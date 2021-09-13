import json
import sys
import math
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.patches as patches
frecCols = []
DELTA_TIME = 0.0005

maxColTime = 0
font = {
            'weight' : 'normal',
            'size'   : 20}

plt.rc('font', **font)
jsons = []
for fname in sys.argv[1:]:
    file = open(fname)
    jsonData = json.load(file)
    jsons.append(jsonData)
    data = jsonData["snapshots"]
    auxMaxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))
    if maxColTime < auxMaxColTime:
        maxColTime = auxMaxColTime

intervals = np.arange(start=0, stop=maxColTime, step=maxColTime/70)
for jsonData in jsons:
    totalCollisions = jsonData["totalCollisions"]
    data = jsonData["snapshots"]
    N = len(data[0]["particles"])
    totalTime = 0
    lastTime = 0
    # maxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))

    hits = np.zeros(len(intervals))
    for iteration in data:
        collisionTime = iteration["event"]["time"]
        # print(collisionTime)
        totalTime += collisionTime #TODO: Revisar y preguntar al profe por el tiempo total (¿el del reloj o el total?)
        for i,interval in enumerate(list(intervals)):
            if collisionTime <= interval:
                hits[i-1] += 1
                break

    # print(intervals)
    print(f"N={N}: Sum of all probabilities: {np.sum(hits/totalCollisions)}")
    plt.scatter(intervals, hits/totalCollisions, label = f"N={N}")

    #1) Parte 1
    print("Colisiones totales/ Tiempo Total : " + str(totalCollisions / totalTime))
    frecCols.append(totalCollisions / totalTime)

    print("Promedio de tiempos de colisiones: " + str(totalTime/totalCollisions))
    
plt.title(f"bin size={maxColTime/70}")
plt.yscale('log')
plt.legend(loc='upper right', borderaxespad=0.)
plt.xlabel("Tiempos")
plt.ylabel("Cantidad de colisiones")
plt.show()

avgFrecCols = sum(frecCols)/len(frecCols)
print("Valor promedio de frecuencia de colisiones para los 3 Ns: " + str(avgFrecCols))


########################## EJERCICIO 2 ##########################

max_mod_vel = 0
for jsonData in jsons:
    data = jsonData["snapshots"]
    last_third = math.ceil(len(data) * 2/3)
    for iteration in data[last_third:]:
        curr_max_vel_mod = max(list(map(lambda p: math.sqrt(p['velX']**2 + p['velY']**2),iteration["particles"][1:])))
        if(curr_max_vel_mod > max_mod_vel): 
            max_mod_vel = curr_max_vel_mod

for k,jsonData in enumerate(jsons):
    totalCollisions = jsonData["totalCollisions"]
    data = jsonData["snapshots"]
    # N = len(data[0]["particles"])
    totalTime = 0
    lastTime = 0
    # maxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))
    # print(maxColTime)
    intervals = np.arange(start=0, stop=max_mod_vel, step=max_mod_vel/40)
    hits = np.zeros(len(intervals))
    last_third = math.ceil(len(data) * 2/3)
    N = len(data[0]["particles"][1:])
    total_velocities = N * len(data[last_third:])
    first_iteration = data[0]
    first_iteration_mod_vel = np.array(list(map(lambda p: math.sqrt(p['velX']**2 + p['velY']**2),first_iteration['particles'][1:])))
    orig_hits = np.zeros(len(intervals))
    for particle_vel_mod in first_iteration_mod_vel:
            for i,interval in enumerate(list(intervals)):
                if particle_vel_mod <= interval:
                    orig_hits[i-1] += 1
                    break
    for iteration in data[last_third:]:
        particles = iteration["particles"][1:]
        particles_vel_mod = list(map(lambda p: math.sqrt(p['velX']**2 + p['velY']**2),particles))
        for particle_vel_mod in particles_vel_mod:
            for i,interval in enumerate(list(intervals)):
                if particle_vel_mod <= interval:
                    hits[i-1] += 1
                    break
    
    # print(intervals)
    print(f"N={N}: Sum of all probabilities: {np.sum(orig_hits/N)}")
    print(f"N={N}: Sum of all probabilities: {np.sum(hits/total_velocities)}")
    if k == 2: plt.plot(intervals,orig_hits/N, marker='o', label=f"orig N={N}", color='red')
    plt.plot(intervals, hits/total_velocities, marker='o', label = f"N={N}")

plt.title(f"bin size={max_mod_vel/40}")
plt.legend(loc='upper right', borderaxespad=0.)
plt.xlabel("Velocidades")
plt.ylabel("Distribución de probabilidad de velocidades")
plt.show()



########################## EJERCICIO 3 ##########################
fig, ax = plt.subplots()
velocities = [1, 2, 3]
for k,jsonData in enumerate(jsons):
    data = jsonData["snapshots"]
    
    bigBoisPos = list(map(lambda iteration: (iteration["particles"][0]["posX"],iteration["particles"][0]["posY"]),data))
        
    plt.plot(list(map(lambda bigboi: bigboi[0], bigBoisPos)), list(map(lambda bigboi: bigboi[1], bigBoisPos)), label = f"V={velocities[k]}")
    start = ax.add_artist(plt.Circle((bigBoisPos[0][0],bigBoisPos[0][1]), 0.1, color='green', alpha=0.3))
    end = ax.add_artist(plt.Circle((bigBoisPos[-1][0],bigBoisPos[-1][1]),  0.1, color='red', alpha=0.3))


plt.legend(loc='upper right', borderaxespad=0.)
plt.xlim(0,6)
plt.ylim(0,6)
plt.xlabel("Pos x")
plt.ylabel("Pos y")
plt.show()