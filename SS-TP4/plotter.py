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


def mqe(exact, aprox):
    steps = len(exact)
    np_exact = np.array(exact)
    np_aprox = np.array(aprox)
    return np.sum(np.square(np_exact - np_aprox))/steps

def ej1(jsons, analytic):
    print(analytic)
    analytic_x = list(map(lambda snapshot: snapshot['particles'][0]['posX'], analytic['snapshots']))
    deltaT = analytic['snapshots'][1]['time'] - analytic['snapshots'][0]['time']
    print(f"dt: {deltaT}")
    for jsonData in jsons:
        method = jsonData['method']
        totalTime = jsonData['totalTime']
        Xs = list(map(lambda snapshot: snapshot['particles'][0]['posX'], jsonData['snapshots']))
        times = list(map(lambda snapshot: snapshot['time'], jsonData['snapshots']))
        # plt.figure()
        plt.plot(times, Xs, label=f"{method}")
        print(f"Mean Square Difference for {method}: {mqe(analytic_x, Xs)}")
    
    plt.plot(times, analytic_x, label="analytic")    
    plt.legend()
    plt.ylabel('posición (m)')
    plt.xlabel('tiempo (s)')
    plt.show()

def ej1_3(jsons):
    for jsonData in jsons:
        method = jsonData['method']
        dcms = jsonData['dcms']
        deltaTs = list(map(lambda dcm: dcm['deltaT'], dcms))
        dcm = list(map(lambda dcm: dcm['dcm'], dcms))
        plt.plot(deltaTs, dcm, '-o', label=f"{method}")
    plt.legend()
    plt.yscale('log')
    plt.ylabel('ECM ($m^2$)')
    plt.xlabel('dt (s)')
    plt.show()



def ej2_1_dt(json):
    for jsonData in jsons:

        deltaTs = list(map(lambda data: data['deltaT'],jsonData))
        timeAndEnergies = list(map(lambda data: data['timeAndEnergies'],jsonData))
        energies_std = []
        fig, ax = plt.subplots()
        ax.ticklabel_format(useOffset=False)
        # ax.plot(times[:len(energies) -2], energies[:len(energies) -2], '-o', label=f"dt={int(deltaT)}")
        for deltaT,timeAndEnergy in zip(deltaTs,timeAndEnergies):
            times = np.array(list(map(lambda data: data['time'], timeAndEnergy))) / (24  * 60 * 60) 
            energies = list(map(lambda data: data['energy'], timeAndEnergy))
            ax.plot(times[:len(energies) -2], energies[:len(energies) -2], 'o', label=f"dt={int(deltaT)}s")
            energies_std.append(np.std(np.array(energies)))


        # plt.yscale('log')
        plt.legend(loc="lower right")
        plt.ylabel('Energía(t) - Energía(0) (J)')
        plt.xlabel('t (días)')
        plt.show()
        fig.savefig('energyVSdtslog', bbox_inches='tight')


        # plt.plot(deltaTs, energies_std,'-o')
        # plt.ylabel('Desvío estándar de la energía')
        # plt.xlabel('dt (s)')
        # plt.show()

def ej2_1a(jsons):
    def plotData(jsonData, state):
        distances = list(map(lambda data: data['targetDistance'],jsonData))
       
        takeOffTimes = list(map(lambda data: data['takeOffTime']/(24*60*60),jsonData))
        #print(marsDistance)
        if state == 'Aterriza':
            marker = 'bo'
        elif state == "Alcanza órbita":
            marker = 'go'
        else:
            marker = 'ro'
        plt.plot(takeOffTimes, distances, marker,label=state)

    for jsonData in jsons:
        landedData = list(filter(lambda data: data['state'] == "LANDED",jsonData))
        inOrbitData = list(filter(lambda data: data['state'] == "IN_ORBIT",jsonData))
        missData = list(filter(lambda data: data['state'] == "MISS",jsonData))
        plotData(landedData, "Aterriza")
        plotData(inOrbitData, "Alcanza órbita")
        plotData(missData, "Falla")
        plt.ylabel(f'Distancia (km)')
        plt.xlabel('Tiempo hasta despegue (dia)')
        plt.legend()
        plt.show()


def ej2_1_b(json):
    totalTime = json['totalTime']
    
    targetDistance = json['targetDistance']
    takeOffTime = json['takeOffTime']
    successful = json['successful']
    target = json['target']
    spaceship_vals = []
    times = []
    for snapshot in json['snapshots']:
        spaceship_data = list(filter(lambda particle: particle['id'] == 1, snapshot['particles']))
        if len(spaceship_data) > 0:
            spaceship_vals.append(spaceship_data[0])
            times.append(snapshot['time'])
    mod_vels = list(map(lambda it: math.sqrt(it['velX']**2 + it['velY']**2), spaceship_vals))
    print(f"Tiempo hasta llegar a la orbita: {times[-1] - takeOffTime} s")
    times = (np.array(times) - takeOffTime) / (24  * 60 * 60)
    print(f"Tiempo hasta llegar a la orbita: {times[-1]} dias")
    plt.plot(times, mod_vels, '-o', label="Módulo de la velocidad de la nave")
    plt.legend()
    plt.ylabel('Módulo de la velocidad de la nave (km/s)')
    plt.xlabel('Tiempo (dias)')
    plt.show()

    last = json['snapshots'][-1]
    last_mars = list(filter(lambda particle: particle['id'] == 2,last['particles']))[0]
    last_spaceship = list(filter(lambda particle: particle['id'] == 1,last['particles']))[0]

    print(f"Velocidad relativa al llegar a {target}: {math.sqrt(last_spaceship['posX']**2 + last_spaceship['posY']**2) - math.sqrt(last_mars['posX']**2 + last_mars['posY']**2)} km/s")
    

def ej2_2(json):

    def plotData(jsonData, state):
        initialVelocities = list(map(lambda data: data['velocity'],jsonData))
        total = len(initialVelocities)
        travelDuration = list(map(lambda data: data['totalTime'] / (24*60*60),jsonData))
        #print(marsDistance)
        if state == 'Aterriza':
            marker = 'bo'
        elif state == "Alcanza órbita":
            marker = 'go'
        else:
            marker = 'ro'
            
        plt.plot(initialVelocities, travelDuration, marker,label=f"{state}")
        #plt.yscale('log')

    for jsonData in jsons:
        landedData = list(filter(lambda data: data['state'] == "LANDED",jsonData))
        inOrbitData = list(filter(lambda data: data['state'] == "IN_ORBIT",jsonData))
        missData = list(filter(lambda data: data['state'] == "MISS",jsonData))
        # successfulData = list(filter(lambda data: data['successful'],jsonData))
        # unsuccessfulData = list(filter(lambda data: not data['successful'],jsonData))
        times = list(map(lambda data: data['totalTime'], landedData))
        min_time = min(times)
        min_vals = list(filter(lambda data: data['totalTime'] == min_time,landedData))[0]
        print(f"Optimized travel time is: {min_vals['totalTime']}, for v0 = {min_vals['velocity']} km/s")
        plotData(landedData, "Aterriza")
        plotData(inOrbitData, "Alcanza órbita")
        plotData(missData, "Falla")
        plt.legend()
        plt.ylabel('Duración del trayecto (días)')
        plt.xlabel('Velocidad inicial (km/s)')
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
        jsons = get_jsons_in_folder(dirs[0])
        
        it = list(filter(lambda json: json['method'] == 'analytic',jsons))
        print(it)
        if len(it) != 0:
            analytic = it[0]
        else:
            exit(-1)
        ej1(list(filter(lambda json: json['method'] != 'analytic', jsons)), analytic)
        jsons = get_jsons_in_folder(dirs[1])
        ej1_3(jsons)
    elif choice == "2":  # Ejercicios 2.1 a,b
       # jsons = get_jsons_in_folder(dirs[0])
        #ej2_1_dt(jsons)
        
        jsons = get_jsons_in_folder(dirs[0])
        ej2_1a(jsons)
        jsons = get_jsons_in_folder(dirs[1])
        if(len(jsons) != 1):
            print(f"folder should contain 1 json for ej2_1b and ej3_1b")
        ej2_1_b(jsons[0])
    
    elif choice == "3": # Ejercicio 2.2
        jsons = get_jsons_in_folder(dirs[0])
        ej2_2(jsons)


    elif choice == "4": # Ejercicio dt optimo
        jsons = get_jsons_in_folder(dirs[0])
        ej2_1_dt(jsons)