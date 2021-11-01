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
            'size'   : 18}

plt.rc('font', **font)

def ej1(json):
    for i,iteration in enumerate(json):
        times = list(map(lambda snapshot: snapshot['time'], iteration))
        arrives = list(map(lambda snapshot: snapshot['acumExited'], iteration))
        plt.plot(times, arrives, '-o', label=f"simulation {i}")
    plt.legend()
    plt.ylabel("Cantidad de particulas")
    plt.xlabel("tiempo (s)")
    plt.show()


def ej2(json):
    timeToArrived = {}
    for iteration in json:
        for snapshot in iteration:
            if(timeToArrived.get(snapshot['acumExited']) == None):
                timeToArrived[snapshot['acumExited']] = list()
            timeToArrived[snapshot['acumExited']].append(snapshot['time'])
    means = []
    std_dev = []
    amounts = list(timeToArrived.keys())
    for amount in amounts:
        means.append(np.mean(np.array(timeToArrived[amount])))
        std_dev.append(np.std(np.array(timeToArrived[amount])))
    plt.errorbar(amounts, means , yerr=std_dev, fmt='o')
    plt.ylabel("Cantidad de particulas")
    plt.xlabel("tiempo promedio (s)")
    plt.show()
    plt.errorbar(means, amounts, xerr=std_dev, fmt='o')
    plt.ylabel("Cantidad de particulas")
    plt.xlabel("tiempo promedio (s)")
    plt.show()
    Qs, times, stdQs = calculateQProm(json)
    # plt.plot(times, Qs)
    plt.errorbar(times, Qs, yerr=stdQs, fmt='o')
    plt.ylabel("Caudal (1/s)")
    plt.xlabel("tiempo (s)")
    # for iteration in json:
    #     Qs, times = calculateQ (iteration, 2)
    #     plt.plot(times, Qs)
    plt.show()

def calculateQProm(results):
    totalQs = []
    totalTimes = []
    for simulation in results:
        Qs, times = calculateQ(simulation, 2)
        totalQs.append(np.array(Qs))
        totalTimes.append(np.array(times))
    _min = float('inf')
    for times in totalTimes:
        cur = times.shape[0]
        if(cur < _min):
            _min = cur
    auxQs = []
    # for Qs in totalQs:
    #     auxQs.append(Qs[:_min])
    # totalTimes = times[:_min]
    totalQs = np.mean(np.array(auxQs), axis=0)
    return totalQs, totalTimes, np.std(np.array(auxQs), axis=0)


def calculateQ (iteration, dt):
    Qs = []
    times = []
    i = 0
    prev_j = 0
    total = len(iteration) - 1
    timeStep = 0.2
    while(prev_j < total):
        acum = 0
        target = i + dt
        for j,snapshot in enumerate(iteration[prev_j:]):
            if(snapshot['time'] >= i and snapshot['time'] <= target):
                acum += snapshot['exited']
            elif(snapshot['time'] > target):
                break
            elif(snapshot['time'] < i):
                prev_j += 1
        Qs.append(acum/dt)
        times.append(i)
        i += 1
    return Qs,times
    
def ej3(json):
    for iteration in json:
        print("Itero xD")
        D = iteration['d']
        N = iteration['n']
        results = iteration['results']
        Qs,times = calculateQProm(results)
        Qs = Qs[::20]
        times = times[::20]
   
        print(f"ahora ploteo xD QLEN= {len(Qs)} TIMES LEN = {len(times)}" )
        plt.plot(Qs,times, label=f"d = {D}, N = {N}")
        print(f"ya plotié")
        break
    plt.legend()
    plt.ylabel("Q promedio (?)")
    plt.xlabel("tiempo (s)")
    plt.show()
    


def ej4(json):
    for iteration in json:
        D = iteration['d']
        N = iteration['n']
        results = iteration['results']
        Qs,times = calculateQProm(results,"exited")
        Qs = Qs[::20]
        times = times[::20]
   
        print(f"ahora ploteo xD QLEN= {len(Qs)} TIMES LEN = {len(times)}" )
        plt.plot(Qs,times, label=f"d = {D}, N = {N}")
        print(f"ya plotié")
        break
    plt.legend()
    plt.ylabel("Q promedio (?)")
    plt.xlabel("tiempo (s)")
    plt.show()

def get_jsons_in_folder(dir):
    jsons = []
    pattern = re.compile("\.json$")
    for fname in list(filter(pattern.search, os.listdir(dir))):
        file = open(os.path.join(dir,fname))
        jsons.append(json.load(file))
    return jsons

if __name__ == "__main__":
    choice = sys.argv[1]
    dirs = []
    for arg in sys.argv[2:]:
        dirs.append(arg)
    # dir = sys.argv[2]
    # jsons = []
    # for fname in list(filter(pattern.search, os.listdir(dir))):
    #     file = open(os.path.join(dir,fname))
    #     jsons.append(json.load(file))

    if choice == "1":
        json = get_jsons_in_folder(dirs[0])[0]
        ej1(json)
    elif choice == "2":
        json = get_jsons_in_folder(dirs[0])[0]
        ej2(json)
    elif choice == "3":
        json = get_jsons_in_folder(dirs[0])[0]
        ej3(json)
    elif choice == "4":
        json = get_jsons_in_folder(dirs[0])[0]
        ej4(json)