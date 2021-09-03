import matplotlib.pyplot as plt
import json 
import sys
import numpy as np
import math

ETA_UNICODE = "\u03B7"
RO_UNICODE = "\u03C1"

def runBenchmarkGraphs(varyEta, varyDensity):
    def sort_using_eta(elem):
        return elem['eta']

    def sort_using_N(elem):
        return elem['n']

    
    def sort_using_RO(elem):
        return elem['density']
    
    f = open(varyEta)

    font = {
            'weight' : 'normal',
            'size'   : 20}

    plt.rc('font', **font)

    jsonVAsData = json.load(f)


    # -------Grafico de VA en funcion de la cantidad de iteraciones-----------------
    

    graphic_index = 0
    VAiters = jsonVAsData["iterations"]
    VaData = sorted(jsonVAsData['data'],key=sort_using_N)
    dataFirstGraphic =  VaData[graphic_index]
    n = dataFirstGraphic["n"]

    etas = sorted(dataFirstGraphic["etas"],key=sort_using_eta)
    
   

    # titleText = "VAs en función del tiempo \nN = " + str(n) + " ETA = " + str(etas[2]["eta"])
    #titleText = f"VAs en función del tiempo con N = {n}"
    #plt.title(titleText)
    plt.xlabel("Iteración")
    plt.ylim(0, 1.4)
    plt.ylabel("Polarización")
 
    for (i,eta) in enumerate(etas):
        
        if i  > 0  and  (i ==1   or  i % 20 == 0):   
            plt.plot(range(VAiters), eta["vas"], "o", label = f'{ETA_UNICODE} = {eta["eta"]}')

    plt.legend(loc='upper right', borderaxespad=0.)
    plt.grid()
    # plt.legend(loc='upper center', bbox_to_anchor=(0.5, -0.05), fancybox=True, shadow=True, ncol=5)
    # plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
    plt.savefig('results/VETA1.png')
    plt.show()

    # -------------------------------------------------------------------------------

  #dataFor2ndTemporalGraph = jsonVAsData['data'][2]
    shapes = ["X","s","o","^","D","v","*"]
    for i,NData in enumerate(VaData):
        n = NData["n"]
        x = []
        y = []
        err = []
        etas = NData['etas']
        VaFrom = int(VAiters*0.8) 
        for eta in etas:
            
            x.append(eta["eta"])
            mean = np.mean(eta["vas"][VaFrom:])
            y.append(mean)
            std = np.std(eta["vas"][VaFrom:])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'N = {n}')
    #plt.title(f"VAs en funcion de {ETA_UNICODE} con {VAiters} iteraciones con {RO_UNICODE} constante")
    plt.xlabel(f"Amplitud del ruido")
    plt.ylabel("Polarización")
    plt.legend(loc='upper right', borderaxespad=0.)
    plt.grid()
    plt.savefig('results/VETA.png')
  
    plt.show()



    f2 = open(varyDensity)

    jsonVAsData = json.load(f2)
    data = sorted(jsonVAsData['data'],key=sort_using_eta)
    VaIters = jsonVAsData['iterations']
    plt.ylim(0, 1.3)
    for i,data in enumerate(data):
        eta = data["eta"]
        x = []
        y = []
        err = []
        densities = data['densities']
        VaFrom = int(VaIters*0.5) 
        for density in densities:
        
            x.append(density["density"])
            mean = np.mean(density["vas"])
            y.append(mean)
            std = np.std(density["vas"])
            err.append(std)
        plt.errorbar(x, y, yerr=err, fmt=shapes[i], label = f'{ETA_UNICODE} = {eta}')
    #plt.title(f"VAs en funcion de la densidad con {VaIters} iteraciones con {ETA_UNICODE} constante")
    plt.xlabel(f"Densidad de partículas")
    plt.ylabel("Polarización")
    plt.legend( loc='upper right', borderaxespad=0.)
    
    plt.grid()
    plt.savefig('results/VD.png')
    plt.show()

# ----------------------------------------------------------------------
   
    dataETA1 = sorted(jsonVAsData['data'],key=sort_using_eta)[1]
    print("eta: "+ str(dataETA1["eta"]))
    densities = sorted(dataETA1["densities"],key=sort_using_RO)
    plt.ylim(0, 1.3)
  
    for i,data in enumerate(densities):
        density = data["density"]
       # print("density: "+str(density))
        if str(density) == "0.1"or str(density) == "2.0"  or str(density) == "5.0" or str(density) == "10.0":
            vas = data["vas"]
            VaIters = len(vas)
            
            plt.plot(range(VaIters), vas,'o', label = f'{RO_UNICODE} = {str(density)}')

    plt.xlabel(f"Iteraciones")
    plt.ylabel("Polarización")
    plt.legend( loc='upper right', borderaxespad=0.)
    
    plt.grid()
    plt.savefig('results/VDFT.png')
    plt.show()

def runSimulation(sim_file):
    f = open(sim_file)

    jsonData = json.load(f)
    data = jsonData["data"]
    iterations = len(data)
    vas = []
    for iteration_data in data:
        particles = iteration_data["particles"]
        amount = len(particles)
        counterX = 0
        counterY = 0
        for particle in particles:
            counterX += math.cos(particle["direction"])
            counterY += math.sin(particle["direction"])
        vas.append((1.0/amount) * math.sqrt((counterX ** 2) + (counterY**2)))
    plt.xlabel("Iteración")
    # plt.ylim(0, 1.4)
    plt.ylabel("Polarización")
    plt.plot(range(iterations), vas, "o")
    plt.show()


if __name__=="__main__":
    mode = sys.argv[1]
    if mode == "s":
        runSimulation(sys.argv[2])
    elif mode == "b":
        runBenchmarkGraphs(sys.argv[2], sys.argv[3])
    else:
        print("First argument must be \"s\" for simulation or \"b\" for benchmark")
        exit(-1)
    

    