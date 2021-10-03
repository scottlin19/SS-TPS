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
        jsons = get_jsons_in_folder(dirs[0])
        # ej2(jsons)


