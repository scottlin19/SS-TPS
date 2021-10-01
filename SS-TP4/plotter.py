import json
import sys
import math
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.patches as patches
import matplotlib.ticker as mticker
import os
import re


font = {
            'weight' : 'normal',
            'size'   : 20}

plt.rc('font', **font)

def ej1(jsons):
    for jsonData in jsons:
        method = jsonData['method']
        totalTime = jsonData['totalTime']
        Xs = list(map(lambda snapshot: snapshot['particles'][0]['posX'], jsonData['snapshots']))
        times = list(map(lambda snapshot: snapshot['time'], jsonData['snapshots']))
        plt.plot(times, Xs, 'o', label=f"{method}")
    plt.legend()
    plt.show()


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
    bin_size = 0.15
    for k,jsonData in enumerate(jsons):
        totalCollisions = jsonData["totalCollisions"]
        data = jsonData["snapshots"]
        # N = len(data[0]["particles"])
        totalTime = 0
        lastTime = 0

        intervals = np.arange(start=0, stop=max_mod_vel, step=bin_size)
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
        plt.plot(intervals, hits/total_velocities, marker='o', label = f"N={N} ultimo tercio")
        if k == 2: 
            plt.plot(intervals,orig_hits/N, marker='o', label=f"N={N} primera iteracion", color='red')
        # plt.title(f"bin size={max_mod_vel/40}")
        plt.legend(loc='upper right', borderaxespad=0.)
        plt.xlabel("Velocidades (m/s)")
        plt.ylabel("Distribución de probabilidad de velocidades")
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
        file = open(os.path.join(dir,fname))
        jsons.append(json.load(file))

    if choice == "1":
        ej1(jsons)
    elif choice == "2":
        ej2(jsons)


