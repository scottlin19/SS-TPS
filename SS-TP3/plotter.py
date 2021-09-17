import json
import sys
import math
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.patches as patches
import os
import re


DELTA_TIME = 0.0005

font = {
            'weight' : 'normal',
            'size'   : 20}

plt.rc('font', **font)

def ej1(jsons):
    frecCols = []
    maxColTime = 0
    for jsonData in jsons:
        data = jsonData["snapshots"]
        auxMaxColTime = max(list(map(lambda iteration: iteration["event"]["time"],data)))
        if maxColTime < auxMaxColTime:
            maxColTime = auxMaxColTime
    bin_size = 0.0005
    intervals = np.arange(start=0, stop=maxColTime, step=0.0005)
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
        no_match_cells = []
        for i,hit in enumerate(hits):
            if hit == 0:
                no_match_cells.append(i)
        intervals = np.delete(intervals, no_match_cells)
        hits = np.delete(hits, no_match_cells)
        plt.plot(intervals, hits/(totalCollisions*bin_size), marker="o", label = f"N={N}")
        # plt.scatter(intervals, hits/totalCollisions, label = f"N={N}")
        print(f"--------------------------------------------")
        print(f"For N = {N}")
        #1) Parte 1
        print("Colisiones totales/ Tiempo Total : " + str(totalCollisions / totalTime))
        frecCols.append(totalCollisions / totalTime)

        print("Promedio de tiempos de colisiones: " + str(totalTime/totalCollisions))
    
    # plt.title(f"bin size={maxColTime/50}")
    plt.yscale('log')
    plt.legend(loc='upper right', borderaxespad=0.)
    plt.xlabel("Tiempos (ms)")
    plt.ylabel("Cantidad de colisiones")
    plt.show()

    avgFrecCols = sum(frecCols)/len(frecCols)
    print(f"--------------------------------------------")
    print("Valor promedio de frecuencia de colisiones para los 3 Ns: " + str(avgFrecCols))


########################## EJERCICIO 2 ##########################
def ej2(jsons):
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
        # print(f"N={N}: Sum of all probabilities: {np.sum(orig_hits/N)}")
        # print(f"N={N}: Sum of all probabilities: {np.sum(hits/total_velocities)}")
        if k == 2: plt.plot(intervals,orig_hits/N, marker='o', label=f"orig N={N}", color='red')
        plt.plot(intervals, hits/total_velocities, marker='o', label = f"N={N}")

    plt.title(f"bin size={max_mod_vel/40}")
    plt.legend(loc='upper right', borderaxespad=0.)
    plt.xlabel("Velocidades")
    plt.ylabel("Distribución de probabilidad de velocidades")
    plt.show()



########################## EJERCICIO 3 ##########################

def ej3(jsons):
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


########################## EJERCICIO 4 ##########################

def ej4(jsons):

    ##################### Big Boi ############################
    # Asumimos datos iguales para todos los jsons
    total = len(jsons[0]['snapshots'])
    totalTime = jsons[0]['totalTime']
    clockStep = jsons[0]['totalTime']/total
    # start_pos = []
    # for jsonData in jsons:
    #     start_pos.append((jsonData['snapshots'][0]['particles'][0]['posX'],jsonData['snapshots'][0]['particles'][0]['posY']))
    promAcum = []
    desvAcum = []
    for i in range(0,total):
        norms2 = []
        for j, jsonData in enumerate(jsons):
            norms2.append((jsonData['snapshots'][i]['particles'][0]['posX'] - jsonData['snapshots'][0]['particles'][0]['posX'])**2 + (jsonData['snapshots'][i]['particles'][0]['posY'] - jsonData['snapshots'][0]['particles'][0]['posY'])**2)
        norms2 = np.array(norms2)
        promAcum.append(np.mean(norms2))
        desvAcum.append(np.std(norms2))
    intervals = np.arange(start=0, stop=totalTime, step=clockStep)
    plt.errorbar(intervals, promAcum, yerr=desvAcum, marker='o')
    plt.show()
    
    ##################### Not Big Boi ############################
    data = jsons[0]
    # start_pos = []
    # for particles in data['snapshots'][0]['particles'][1:]:
    #     start_pos.append((particles['posX'],particles['posY']))
    promAcum = []
    desvAcum = []
    for i in range(0,total):
        norms2 = []
        for j, particles in enumerate(data['snapshots'][i]['particles'][1:]):
            # print(j)
            # if j > 0:
            norms2.append((particles['posX'] - data['snapshots'][0]['particles'][j+1]['posX'])**2 + (particles['posY'] - data['snapshots'][0]['particles'][j+1]['posY'])**2)
        # for j, jsonData in enumerate(jsons):
        #     norms2.append((jsonData['snapshots'][i]['particles'][0]['posX'] - start_pos[j][0])**2 + (jsonData['snapshots'][i]['particles'][0]['posY'] - start_pos[j][1])**2)
        norms2 = np.array(norms2)
        promAcum.append(np.mean(norms2))
        desvAcum.append(np.std(norms2))
    intervals = np.arange(start=0, stop=totalTime, step=clockStep)
    plt.ylabel("DCM")
    plt.xlabel("Tiempo (s)")
    plt.errorbar(intervals, promAcum, yerr=desvAcum, marker='o')
    plt.show()
# File1 --->  T1, T2, T3, T4
# T1 ----> P1, P2, P3

# P1 - P10 ^2
# P2 - P20 ^2
# P3 - P30 ^2
# -------------

if __name__ == "__main__":
    choice = sys.argv[1]
    dir = sys.argv[2]
    pattern = re.compile("\.json$")
    jsons = []
    for fname in list(filter(pattern.search, os.listdir(dir))):
        print(dir + fname)
        file = open(os.path.join(dir,fname))
        jsons.append(json.load(file))

    if choice == "1":
        ej1(jsons)
    elif choice == "2":
        ej2(jsons)
    elif choice == "3":
        ej3(jsons)
    elif choice == "4":
        ej4(jsons)


