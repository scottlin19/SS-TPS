import matplotlib.pyplot as plt
import json 
import sys
import numpy as np


ETA_UNICODE = "\u03B7"

if __name__=="__main__":
    
    f = open(sys.argv[1])

    jsonVAsData = json.load(f)


    # -------Grafico de VA en funcion de la cantidad de iteraciones-----------------
    
    dataFor1stGraph = jsonVAsData['data'][2]
    n = dataFor1stGraph["n"]
    etas = dataFor1stGraph["etas"]
    avgs = etas[2]["avgs"]
    iterations = len(avgs)

    # titleText = "VAs en función del tiempo \nN = " + str(n) + " ETA = " + str(etas[2]["eta"])
    titleText = "VAs en función del tiempo"
    plt.title(titleText)
    plt.xlabel("Iteración")
    plt.ylim(0, 1.2)
    plt.ylabel("Va Promedio")

    stds = etas[2]["stds"]
 
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

    plt.legend(bbox_to_anchor=(1.005, 1), loc='upper left', borderaxespad=0.)
    # plt.legend(loc='upper center', bbox_to_anchor=(0.5, -0.05), fancybox=True, shadow=True, ncol=5)
    # plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    plt.show()

    # -------------------------------------------------------------------------------

  #dataFor2ndTemporalGraph = jsonVAsData['data'][2]
    shapes = ["X","s","o","^","D","v","*"]
    for i,NData in enumerate(jsonVAsData['data']):
        n = NData["n"]
        x = []
        y = []
        err = []
        etas = NData['etas']
        VaIters = len(eta['avgs'])
        VaFrom = int(VaIters*0.75)
        
        for eta in etas: 
            x.append(eta["eta"])
            mean = np.mean(eta["avgs"][VaFrom:])
            y.append(mean)
            std = np.std(eta["avgs"][VaFrom:])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'N = {n}')
    plt.title(f"VAs en funcion de {ETA_UNICODE} con {iterations} iteraciones")
    plt.xlabel(f"{ETA_UNICODE}")
    plt.ylabel("Va Promedio")
    plt.legend(bbox_to_anchor=(1.005, 1), loc='upper left', borderaxespad=0.)
    plt.show()