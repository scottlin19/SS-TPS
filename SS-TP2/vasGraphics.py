import matplotlib.pyplot as plt
import json 
import sys
import numpy as np


ETA_UNICODE = "\u03B7"

if __name__=="__main__":

    def sort_using_eta(elem):
        return elem['eta']
    
    f = open(sys.argv[1])

    jsonVAsData = json.load(f)


    # -------Grafico de VA en funcion de la cantidad de iteraciones-----------------
    
    dataFor1stGraph = jsonVAsData['data'][2]
    n = dataFor1stGraph["n"]

    etas = sorted(dataFor1stGraph["etas"],key=sort_using_eta)
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
        for eta in etas:
            VaIters = len(eta['avgs'])
            VaFrom = int(VaIters*0.75) 
            x.append(eta["eta"])
            mean = np.mean(eta["avgs"][VaFrom:])
            y.append(mean)
            std = np.std(eta["avgs"][VaFrom:])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'N = {n}')
    plt.title(f"VAs en funcion de {ETA_UNICODE} con {iterations} iteraciones")
    plt.xlabel(f"eta")
    plt.ylabel("Va Promedio")
    plt.legend(bbox_to_anchor=(1.005, 1), loc='upper left', borderaxespad=0.)
    plt.show()


     # -------------------------------------------------------------------------------

    f2 = open(sys.argv[2])

    jsonVAsData = json.load(f2)
    data = sorted(jsonVAsData['data'],key=sort_using_eta)
    for i,data in enumerate(data):
        eta = data["eta"]
        x = []
        y = []
        err = []
        densities = data['densities']
        for density in densities:
            VaIters = len(density['avgs'])
            VaFrom = int(VaIters*0.75) 
            x.append(density["density"])
            mean = np.mean(density["avgs"][VaFrom:])
            y.append(mean)
            std = np.std(density["avgs"][VaFrom:])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'{ETA_UNICODE} = {eta}')
    plt.title(f"VAs en funcion de la densidad con {iterations} iteraciones")
    plt.xlabel("densidad")
    plt.ylabel("Va Promedio")
    plt.legend(bbox_to_anchor=(1.005, 1), loc='upper left', borderaxespad=0.)
    plt.show()

    