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


def mqe(exact, aprox):
    steps = len(exact)
    np_exact = np.array(exact)
    np_aprox = np.array(aprox)
    return np.sum(np.square(np_exact - np_aprox))/steps

def ej1(jsons, analytic):
    print(analytic)
    analytic_x = list(map(lambda snapshot: snapshot['particles'][0]['posX'], analytic['snapshots']))
    for jsonData in jsons:
        method = jsonData['method']
        totalTime = jsonData['totalTime']
        Xs = list(map(lambda snapshot: snapshot['particles'][0]['posX'], jsonData['snapshots']))
        times = list(map(lambda snapshot: snapshot['time'], jsonData['snapshots']))
        plt.figure()
        plt.plot(times, Xs, '-o', label=f"{method}")
        plt.plot(times, analytic_x,'-o', label="analytic")
        print(f"Mean Square Difference for {method}: {mqe(analytic_x, Xs)}")
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
    plt.ylabel('DCM')
    plt.xlabel('delta t (s)')
    plt.show()



def ej2_1(jsons):
    for jsonData in jsons:
        takeOffTime = list(map(lambda data: data['takeOffTime'],jsonData))
        total = len(takeOffTime)
        takeOffTime = takeOffTime[:int(total/2)]
        marsDistance = list(map(lambda data: data['marsDistance'],jsonData))[:int(total/2)]
        #print(marsDistance)
        plt.plot(takeOffTime, marsDistance, '-o', label="Distance to Mars")
        plt.legend()
        #plt.yscale('log')
        plt.ylabel('Distance to Mars (Km)')
        plt.xlabel('Takeoff time (s)')
        plt.show()

def ej2_1_dt(json):
    for jsonData in jsons:
        deltaT = list(map(lambda data: data['deltaT'],jsonData))
       
        #takeOffTime = takeOffTime[:int(total/2)]
        energy_means = list(map(lambda data: np.mean(data['energies']),jsonData))
        #print(marsDistance)
        energies_diff = []
        for i in range(1,len(energy_means)):
            energies_diff.append(energy_means[i] - energy_means[i-1])

        plt.plot(deltaT[1:], energies_diff, '-o', label="Energy Standard Deviation")
        plt.legend()
        #plt.yscale('log')
        plt.ylabel('Energy Standard Deviation (?)')
        plt.xlabel('dT (s)')
        plt.show()


def ej2_1_b(json):
    totalTime = json['totalTime']
    marsDistance = json['marsDistance']
    takeOffTime = json['takeOffTime']
    successful = json['successful']
    spaceship_vals = []
    times = []
    for snapshot in json['snapshots']:
        spaceship_data = list(filter(lambda particle: particle['id'] == 1, snapshot['particles']))
        if len(spaceship_data) > 0:
            spaceship_vals.append(spaceship_data[0])
            times.append(snapshot['time'])
    mod_vels = list(map(lambda it: math.sqrt(it['posX']**2 + it['posY']**2), spaceship_vals))
    plt.plot(times, mod_vels, '-o', label="Modulo de la velocidad de la nave")
    plt.legend()
    # plt.yscale('log')
    plt.ylabel('Modulo de la velocidad de la nave (km/s)')
    plt.xlabel('Takeoff time (s)')
    plt.show()

    last = json['snapshots'][-1]
    last_mars = list(filter(lambda particle: particle['id'] == 2,last['particles']))[0]
    last_spaceship = list(filter(lambda particle: particle['id'] == 1,last['particles']))[0]

    print(f"Velocidad relativa al llegar a Marte: {math.sqrt(last_spaceship['posX']**2 + last_spaceship['posY']**2) - math.sqrt(last_mars['posX']**2 + last_mars['posY']**2)} km/s")
    

def ej2_2(json):

    def plotData(jsonData, successful):
        initialVelocities = list(map(lambda data: data['velocity'],jsonData))
        total = len(initialVelocities)
        travelDuration = list(map(lambda data: data['totalTime'],jsonData))
        #print(marsDistance)
        if successful:
            marker = '-bo'
        else:
            marker = '-ro'
            
        plt.plot(initialVelocities, travelDuration, marker)
        #plt.yscale('log')

    for jsonData in jsons:
        successfulData = list(filter(lambda data: data['successful'],jsonData))
        unsuccessfulData = list(filter(lambda data: not data['successful'],jsonData))
        plotData(successfulData, True)
        plotData(unsuccessfulData, False)
        
        plt.ylabel('Travel Duration (s)')
        plt.xlabel('Initial Velocity (km/s)')
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
    elif choice == "2":
       # jsons = get_jsons_in_folder(dirs[0])
        #ej2_1_dt(jsons)
        
        jsons = get_jsons_in_folder(dirs[0])
        ej2_1(jsons)
        jsons = get_jsons_in_folder(dirs[1])
        if(len(jsons) != 1):
            print(f"folder should contain 1 json for ej2_1b")
        ej2_1_b(jsons[0])
    
    elif choice == "3":
        jsons = get_jsons_in_folder(dirs[0])
        ej2_2(jsons)


