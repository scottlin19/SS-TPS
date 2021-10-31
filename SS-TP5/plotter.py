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
    Qs, times = calculateQ(json[0])
    plt.plot(times, Qs)
    plt.show()

def calculateQ (iteration):
    Qs = []
    times = []

    for i,snapshot in enumerate(iteration):
        if(i > 0):
            prev = iteration[i-1]['acumExited']
            curr = iteration[i]['acumExited']
            time = snapshot['time']
            Qs.append((curr - prev)/time)
            times.append(time)

    return Qs,times
    
        

def ej3(json):
    for iteration in json:
        print("Itero xD")
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
    

def calculateQProm (results, exitedField):
 
    times = []
    QProms = np.array([])
    print("Arrancó QProm")
    for i, result in enumerate(results):
        snapshots = result['snapshots']
        Qs = np.array([])
        for j in range(len(snapshots)-1):
            next = snapshots[j+1]
            curr = snapshots[j]
            nextExited = len(next[exitedField])
            currExited = len(curr[exitedField])
            dN = nextExited-currExited
            time = next['time']
            Qs.append(dN/time)
            if i == 0:
                times.append(time)
        
    print("Terminó QProm")
    return Qs,times
    
        

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