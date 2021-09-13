import json
import sys
import math
import matplotlib.pyplot as plt
import numpy as np

frecCols = []
DELTA_TIME = 0.0005

maxColTime = 0
jsons = []
for fname in sys.argv[1:]:
    file = open(fname)
    jsonData = json.load(file)
    jsons.append(jsonData)
    data = jsonData["data"]
    auxMaxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))
    if maxColTime < auxMaxColTime:
        maxColTime = auxMaxColTime

for jsonData in jsons:
    totalCollisions = jsonData["totalCollisions"]
    data = jsonData["data"]
    N = len(data[0]["particles"])
    totalTime = 0
    lastTime = 0
    # maxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))
    print(maxColTime)
    intervals = np.arange(start=0, stop=maxColTime, step=maxColTime/100)
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

    plt.scatter(intervals, hits, label = f"N={N}")

    #1) Parte 1
    frecCols.append(totalCollisions / totalTime)

plt.legend(loc='upper right', borderaxespad=0.)
plt.xlabel("Tiempos")
plt.ylabel("Cantidad de colisiones")
plt.show()

avgFrecCols = sum(frecCols)/len(frecCols)
print("Valor promedio de frecuencia de colisiones para los 3 Ns: " + str(avgFrecCols))


########################## EJERCICIO 2 ##########################

max_mod_vel = 0
for jsonData in jsons:
    data = jsonData["data"]
    last_third = math.ceil(len(data) * 2/3)
    for iteration in data[last_third:]:
        curr_max_vel_mod = max(list(map(lambda p: math.sqrt(p['velX']**2 + p['velY']**2),iteration["particles"][1:])))
        if(curr_max_vel_mod > max_mod_vel): 
            max_mod_vel = curr_max_vel_mod

for jsonData in jsons:
    totalCollisions = jsonData["totalCollisions"]
    data = jsonData["data"]
    # N = len(data[0]["particles"])
    totalTime = 0
    lastTime = 0
    # maxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))
    # print(maxColTime)
    intervals = np.arange(start=0, stop=max_mod_vel, step=max_mod_vel/100)
    hits = np.zeros(len(intervals))
    last_third = math.ceil(len(data) * 2/3)
    print(f"original:{len(data)}, last third: {last_third}")
    N = len(data[0]["particles"][1:])
    total_velocities = N * len(data[last_third:])
    for iteration in data[last_third:]:
        particles = iteration["particles"][1:]
        particles_vel_mod = list(map(lambda p: math.sqrt(p['velX']**2 + p['velY']**2),particles))
        for particle_vel_mod in particles_vel_mod:
            for i,interval in enumerate(list(intervals)):
                if particle_vel_mod <= interval:
                    hits[i-1] += 1
                    break
    
    # print(intervals)
    plt.scatter(intervals, hits/total_velocities, label = f"N={N}")

plt.legend(loc='upper right', borderaxespad=0.)
plt.xlabel("Velocidades")
plt.ylabel("Distribución de probabilidad de velocidades")
plt.show()
