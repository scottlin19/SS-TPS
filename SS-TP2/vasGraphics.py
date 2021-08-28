import matplotlib.pyplot as plt
import json 
import sys
import numpy as np


ETA_UNICODE = "\u03B7"
RO_UNICODE = "\u03C1"

if __name__=="__main__":

    def sort_using_eta(elem):
        return elem['eta']
    
    f = open(sys.argv[1])

    jsonVAsData = json.load(f)


    # -------Grafico de VA en funcion de la cantidad de iteraciones-----------------
    

    graphic_index = 2
    dataFor1stGraph = jsonVAsData['data'][graphic_index]
    n = dataFor1stGraph["n"]

    etas = sorted(dataFor1stGraph["etas"],key=sort_using_eta)
    
    VAiters = jsonVAsData["iterations"]

    # titleText = "VAs en función del tiempo \nN = " + str(n) + " ETA = " + str(etas[2]["eta"])
    titleText = f"VAs en función del tiempo con N = {n}"
    plt.title(titleText)
    plt.xlabel("Iteración")
    plt.ylim(0, 1.2)
    plt.ylabel("Va Promedio")
 
    for (i,eta) in enumerate(etas):
        print(i)
        if i % 10 == 0:
            vas = eta["vas"]
            x = range(VAiters)
            y = vas      
            plt.plot(x, y, "o", label = f'{ETA_UNICODE} = {eta["eta"]}')

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
        VaFrom = int(VAiters*0.75) 
        for eta in etas:
            
            x.append(eta["eta"])
            mean = np.mean(eta["vas"][VaFrom:])
            y.append(mean)
            std = np.std(eta["vas"][VaFrom:])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'N = {n}')
    plt.title(f"VAs en funcion de {ETA_UNICODE} con {VAiters} iteraciones con {RO_UNICODE} constante")
    plt.xlabel(f"{ETA_UNICODE}")
    plt.ylabel("Va Promedio")
    plt.legend(bbox_to_anchor=(1.005, 1), loc='upper left', borderaxespad=0.)
    plt.show()


     # -------------------------------------------------------------------------------

    f2 = open(sys.argv[2])

    jsonVAsData = json.load(f2)
    data = sorted(jsonVAsData['data'],key=sort_using_eta)
    VaIters = jsonVAsData['iterations']
    for i,data in enumerate(data):
        eta = data["eta"]
        x = []
        y = []
        err = []
        densities = data['densities']
        VaFrom = int(VaIters*0.75) 
        for density in densities:
        
            x.append(density["density"])
            mean = np.mean(density["vas"][VaFrom:])
            y.append(mean)
            std = np.std(density["vas"][VaFrom:])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'{ETA_UNICODE} = {eta}')
    plt.title(f"VAs en funcion de la densidad con {VaIters} iteraciones con {ETA_UNICODE} constante")
    plt.xlabel(f"{RO_UNICODE}")
    plt.ylabel("Va Promedio")
    plt.legend(bbox_to_anchor=(1.005, 1), loc='upper left', borderaxespad=0.)
    plt.show()

    