package arbolbalanceado;

public class arbolB {

    static int grado; // grado
    Nodo raiz;  //nodo raiz del arbol

    public arbolB(int grado) { //Contructor
        this.grado = grado;
        raiz = new Nodo(grado, null);//nodo nuevo con padre null
    }

// metodo que busca un nodo y devuelve el nodo como resultado                                           |
    public Nodo buscar(Nodo raiz, int valor) {
        int i = 0;//se inicia buscando en el index 0

        while (i < raiz.contador && valor > raiz.valor[i]) {
            i++; //buscar la posicion del valor buscado
        }

        if (i <= raiz.contador && valor == raiz.valor[i]) {
            return raiz; //si el valor se encuentra en este nodo, se devuelve el nodo actual
        }

        if (raiz.hoja) {   //si se busca en la ultima hoja y no se encuentra el valor se retorna nulo
            return null;
        } else {   //si el nodo no es hoja, se sigue buscando recursivamente
            return buscar(raiz.getHijo(i), valor);
        }
    }

    private void dividir(Nodo x, int i, Nodo y) {
        Nodo nuevo = new Nodo(grado, null);//se crea un nuevo nodo
        nuevo.hoja = y.hoja;//iguala propiedades de hoja
        nuevo.contador = grado - 1;//actualiza el tamaño del nodo

        for (int j = 0; j < grado - 1; j++) {
            nuevo.valor[j] = y.valor[j + grado]; //Copia el final de Y en el inicio de z
        }
        if (!y.hoja)//si no es hoja tenemos que desplazar los hijo.
        {
            for (int k = 0; k < grado; k++) {
                nuevo.hijo[k] = y.hijo[k + grado]; //Trasladamos los hijos de y
            }
        }
        y.contador = grado - 1; //Asigna el numero de valores de y

        for (int j = x.contador; j > i; j--) {
            x.hijo[j + 1] = x.hijo[j]; //Desplazar los hijos de x
        }
        
        x.hijo[i + 1] = nuevo; //desplazamos hijo i+1 de x

        //corre todas las claves hacia la derecha
        if (x.contador <= grado - 1) {
            for (int j = x.contador; j >= i; j--) {//en caso de tener menos de la mitad
                x.valor[j + 1] = x.valor[j]; // cambiar valores
            }
        } else {
            for (int j = x.contador - 1; j > i; j--) {///en caso de tener mas de la mitad
                x.valor[j + 1] = x.valor[j]; //cambiar valores
            }
        }
        x.valor[i] = y.valor[grado - 1];//finalmente insertamos el valor en la raiz
        y.valor[grado - 1] = 0; //borramos el valor insertado

        for (int j = 0; j < grado - 1; j++) {
            y.valor[j + grado] = 0; //Borra los valores trasladados
        }
        x.contador++;  //incrementa el numero de valores
    }
    
    
    private void insertarValor(Nodo x, int valor) {
        int i = x.contador; //i es el numero de valores en el nodo X
        if (x.hoja) {
            while (i >= 1 && valor < x.valor[i - 1])//buscar la posición de insercion
            {
                x.valor[i] = x.valor[i - 1];//desplaza los valores
                i--;//desde posición maxima a minima
            }

            x.valor[i] = valor;//Luego de ordenar se inserta el valor
            x.contador++; //incrementar el numero de hijos
        } else {
            int j = 0;
            while (j < x.contador && valor > x.valor[j]) {   //Busca la posición del hijo a recorrer			             		
                j++;
            }
            if (x.hijo[j].contador == grado * 2 - 1) {
                dividir(x, j, x.hijo[j]);//Divide el nodo x, index j
                if (valor > x.valor[j]) {
                    j++;
                }
            }
            insertarValor(x.hijo[j], valor);//Recursividad
        }
    }

    public void insertar(arbolB ab, int valor) {
        Nodo raiz_ant = ab.raiz;//Clonamos la raiz 

        if (raiz_ant.contador == 2 * grado - 1)//Si el nodo esta lleno
        {
            Nodo nuevo = new Nodo(grado, null);//nuevo nodo
            ab.raiz = nuevo; //el nodo vacio será la nueva raiz

            nuevo.hoja = false;//Se asignan valores iniciales
            nuevo.contador = 0;
            nuevo.hijo[0] = raiz_ant;//la raiz antigua será el nuevo hijo

            dividir(nuevo, 0, raiz_ant);//dividir la raiz puesto que esta lleno el nodo
            insertarValor(nuevo, valor); //llamada al metodo insertar
        } else {
            insertarValor(raiz_ant, valor);//Si el nodo no esta lleno se inserta el valor
        }
    }


    
    public void print(Nodo n) {
        for (int i = 0; i < n.contador; i++) {
            System.out.print(n.getValor(i) + " ");//esta parte imprime el nodo raiz
        }

        if (!n.hoja)//si en caso la raiz no es hoja;
        {
            for (int j = 0; j <= n.contador; j++)//recorremos de forma recursiva el nodo
            {				
                if (n.getHijo(j) != null) //preorden .
                {			
                    System.out.println();	
                    print(n.getHijo(j));    //de la izquierda a la derecha
                }
            }
        }
    }

    public void SearchPrintNode(arbolB T, int x) {
        Nodo temp = new Nodo(grado, null);

        temp = buscar(T.raiz, x);

        if (temp == null) {

            System.out.println("El valor no existe");
        } else {

            print(temp);
        }

    }



    ///////////////////////////////////
    class Nodo {
        private int t;  //variable para determinar el grado del árbol
        private int contador; // numero de claves en el nodo
        private int valor[];  // arreglo de claves 
        private Nodo hijo[]; //arreglo de hijos
        private boolean hoja; //conocer si el nodo es hoja o no
        private Nodo padre;  //padre del nodo actual

        
        private Nodo(int t, Nodo padre) {
            this.t = t;  //asigna tamaño o grado
            this.padre = padre; //asignación de padre
            valor = new int[2 * t - 1];  //asignar tamaño 2t
            hijo = new Nodo[2 * t]; // asginar hijos
            hoja = true; //todos los nodos son hojas al iniciar
            contador = 0; //contador inicial en cero
        }

        public int getValor(int index) {//obtiene un valor del nodo
            return valor[index];
        }

        
        public Nodo getHijo(int index) { //obtiene un hijo del nodo
            return hijo[index];
        }
    }
}
