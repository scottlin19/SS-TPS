import matplotlib.pyplot as plt
import json
import statistics 

import numpy as np


ETA_UNICODE = "\u03B7"

if __name__=="__main__":
    
    f = open('./results/message.json')

    jsonVAsData = json.load(f)
    print(jsonVAsData)


    # -------Grafico de VA en funcion de la cantidad de iteraciones-----------------
    
    dataFor1stGraph = jsonVAsData['data'][2]
    n = dataFor1stGraph["n"]
    etas = dataFor1stGraph["etas"]
    avgs = etas[2]["avgs"]
    iterations = len(avgs)

    # titleText = "VAs en función del tiempo \nN = " + str(n) + " ETA = " + str(etas[2]["eta"])
    titleText = "VAs en función del tiempo \n"
    plt.title(titleText)
    plt.xlabel("Iteración")
    plt.ylim(0, 1)
    plt.ylabel("Va Promedio")

    stds = etas[4]["stds"]

    for eta in etas:
        avgs = eta["avgs"]
        stds = eta["stds"]
        x = []
        y = []
        err = []
        for i in range(iterations):
            if (i % 2 == 0 or i < 20):
                x.append(i)
                y.append(avgs[i])
                err.append(stds[i])
        plt.errorbar(x, y, yerr=err, fmt="o", label = f'{ETA_UNICODE} = {eta["eta"]}')
        


    # plt.errorbar(range(iterations), avgs, yerr=stds, fmt="o")
    plt.legend( loc='lower right')
    # plt.legend(loc='upper center', bbox_to_anchor=(0.5, -0.05), fancybox=True, shadow=True, ncol=5)
    # plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    plt.show()

    # -------------------------------------------------------------------------------

    dataFor2ndTemporalGraph = jsonVAsData['data'][2]
    n = dataFor2ndTemporalGraph["n"]
    x = []
    y = []
    err = []
    for eta in etas: 
        x.append(eta["eta"])
        y.append(np.mean(eta["avgs"][500:]))
        err.append(np.mean(eta["stds"][500:]))
    
    plt.errorbar(x, y, yerr=err, fmt="o", label = f'{ETA_UNICODE} = {eta["eta"]}')
    plt.show()