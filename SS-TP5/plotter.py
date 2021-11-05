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



def getDescarga(json):
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
    return amounts, means, std_dev

def ej2(json):
    amounts, means, std_dev = getDescarga(json)
    plt.errorbar(amounts, means , yerr=std_dev, fmt='o')
    plt.ylabel("Cantidad de particulas")
    plt.xlabel("tiempo promedio (s)")
    plt.show()
    plt.errorbar(means, amounts, xerr=std_dev, fmt='o')
    plt.ylabel("Cantidad de particulas")
    plt.xlabel("tiempo promedio (s)")
    plt.show()
    Qs, times = calculateQ2(amounts, means, 2)
    plt.plot(times, Qs)
    plt.show()
    # Qs, times, stdQs = calculateQProm(json)
    # # plt.plot(times, Qs)
    # plt.errorbar(times, Qs, yerr=stdQs, fmt='o')
    # plt.ylabel("Caudal (1/s)")
    # plt.xlabel("tiempo (s)")
    # # for iteration in json:
    # #     Qs, times = calculateQ (iteration, 2)
    # #     plt.plot(times, Qs)
    # plt.show()

def calculateQ2(exiteds, times, dt):
    Qs = []
    aux_times = []
    i = 0
    total = len(times) - 1
    print("total times: "+str(total))
    timeStep = 0.2
    end = False
    while(not end):
        acum = 0
        target = i + dt
        j = 0
        vals = []
        for exited, time in zip(exiteds, times):
            if(time >= i and time <= target):
                vals.append(exited)
            elif(time > target):
                acum = vals[-1] - vals[0] 
                break
            j += 1
            if(j == total):
                end = True
            
        if acum != 0:

            Qs.append(acum/dt)
            aux_times.append(i)
        i += timeStep
    return Qs,aux_times



def calculateQ (snapshots, dt):
    Qs = []
    times = []
    i = 0
    prev_j = 0
    total = len(snapshots) - 1
    timeStep = 0.2
    while(prev_j < total):
        acum = 0
        target = i + dt
        for j,snapshot in enumerate(snapshots[prev_j:]):
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
    Ds = []
    Ns = []
    #mediumRadiuses = []
    totalQs = []
    totalTimes = []
    for iteration in json:
        D = iteration['d']
        Ds.append(D)

        N = iteration['n']
        Ns.append(N)
        

        amounts, means, std_dev = getDescarga(iteration['exitedAndTimes'])
    
        plt.errorbar(means, amounts, xerr=std_dev, fmt='o')
        plt.ylabel("Cantidad de particulas")
        plt.xlabel("tiempo promedio (s)")

        plt.show()
 
        Qs, times = calculateQ2(amounts, means, 5)
        totalQs.append(Qs)
        totalTimes.append(times)

    for i in range(len(totalQs)):
        plt.plot(totalTimes[i], totalQs[i], '-o', label=f"d = {Ds[i]}, N = {Ns[i]}")
    plt.ylabel("Q (1/s)")
    plt.xlabel("tiempo (s)")
    plt.legend()
    plt.show()

    Qproms = []
    Qstds = []
    for i,Qs in enumerate(totalQs):
        times = totalTimes[i]
        Qacum = []
        for j, Q in enumerate(Qs):
           
            if i == 0:
                if times[j] >= 15 and times[j] <= 45:
                    Qacum.append(Q)
            elif i == 1:
                if times[j] >= 15 and times[j] <= 60:
                    Qacum.append(Q)
            elif i == 2:
                if times[j] >= 5 and times[j] <= 70:
                    Qacum.append(Q)
            elif i == 3:
                if times[j] >= 5 and times[j] <= 80:
                    Qacum.append(Q)            
            
        Qproms.append(np.mean(Qacum))
        Qstds.append(np.std(Qacum))
    for i, Qprom in enumerate(Qproms):
        print(f"Qprom {Qprom} for D = {Ds[i]}")
        plt.errorbar(Ds, Qproms , yerr=Qstds, fmt='o')
        plt.ylabel("Q promedio (1/s)")
        plt.xlabel("d (m)")
    plt.show()


    Bs = np.arange(start=-5, stop=5, step=0.01)

    errors = []
    _min = float('inf')
    _min_error = float('inf')
    for i in range(len(Bs)):
        E = 0
        for j in range(len(Qproms)):
            E+= (Qproms[j] - Bs[i]*(Ds[j])**1.5)**2
        errors.append(E)
        if E < _min_error:
            _min = Bs[i]
            _min_error = E
    
    print(f"Min error: {_min_error}")
    print(f"Min B: {_min}")
    plt.figure()
    plt.ylabel("Error ($m^2$)")
    plt.xlabel("Pendiente de ajuste ($m^2$/s)")
    plt.plot(Bs, errors)
    plt.show()
    
    def not_linear_error(min_xs,intervals):
        return min_xs*intervals**1.5


    print(f"min B: {_min}")
    plt.plot(Ds, not_linear_error(_min, np.array(Ds)))
    for i, Qprom in enumerate(Qproms):
        plt.errorbar(Ds, Qproms , yerr=Qstds, fmt='o')
        plt.ylabel("<Q> (1/s)")
        plt.xlabel("d (m)")
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