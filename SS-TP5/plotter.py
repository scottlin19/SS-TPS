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
    timeToArrived = {}
    for i,iteration in enumerate(json):
        # for snapshot in iteration:
        #     if(timeToArrived.get(snapshot['time']) == None):
        #         timeToArrived[snapshot['time']] = list()
        #     timeToArrived[snapshot['time']].append(snapshot['acumExited'])
        times = list(map(lambda snapshot: snapshot['time'], iteration))
        arrives = list(map(lambda snapshot: snapshot['acumExited'], iteration))
        plt.plot(times, arrives, '-o', label=f"simulation {i}")
    plt.legend()
    plt.show()

def ej3(json):
    for iteration in json:
        D = iteration['D']
        N = iteration['N']
        results = iteration['results']
        for result in results:
            snapshots = result['snapshots']
            

        
    plt.show()

def calculateQ (result):
    snapshots = result['snapshots']
    dNs = []
    for i in range(len(snapshots)):
        



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