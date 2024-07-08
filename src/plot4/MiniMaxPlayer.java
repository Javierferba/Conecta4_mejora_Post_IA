/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plot4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author José María Serrano
 * @version 1.7 Departamento de Informática. Universidad de Jáen 
 * Última revisión: 2023-03-30
 *
 * Inteligencia Artificial. 2º Curso. Grado en Ingeniería Informática
 *
 * Clase MiniMaxPlayer para representar al jugador CPU que usa una técnica de IA
 *
 * Esta clase es en la que tenemos que implementar y completar el algoritmo
 * MiniMax
 *
 */
public class MiniMaxPlayer extends Player {

    /**
     * @brief Arbol de n hijos.
     */
    class Arbol_N_Ario {

        /**
         * @brief Clase nodo del arbol
         */
        class Nodo {
            private byte valor = 0; /// Indica quien gana en ese caso: 1=jugador, -1=IA, 0=empate o nodo de paso.
            private byte profundidad; /// Profundidad a la que se encuentra el nodo
            private byte columna = -1;/// Indica en que columna se lanza la ficha para llegar a ese estado.
            private byte[][] _estado;/// Matriz con el estado del tablero.
            private ArrayList<Nodo> _hijos;/// Lista que contiene hasta N hijos del nodo.

            /**
             * @brief Constructor.
             * @param estado
             */
            public Nodo(byte[][] estado) {
                this._estado = estado;
                _hijos = new ArrayList<>();
            }

            /**
             * @brief Inserta un hijo suministrado.
             * @param hijo
             * @return 0 si se ha introducido correctamente o -1 si ha habido algún problema.
             */
            public int insertarHijo(Nodo hijo) {
                if (_hijos.size() < _columnas){
                    _hijos.add(hijo);
                    return 0;
                }
                return -1;
            }

            /**
             * @brief Devuelve el valor columna.
             */
            public int getColumna() {
                return columna;
            }

            /**
             * @brief Devuelve la profundidad.
             */
            public byte getProfundidad() {
                return profundidad;
            }
        }

        private byte _columnas;/// Columnas del tablero de juego.
        private byte _filas;/// Filas del tablero de juego.
        private int _conecta;/// Número de fichas a conectar.
        private Nodo _raiz;/// Caso actual.
        private boolean arbolCompleto = false;

        /**
         * @brief constructor del arbol.
         * @param columnas Columnas del tablero de juego.
         * @param filas  Filas del tablero de juego.
         * @param conecta Número de fichas a conectar.
         * @param estado Estado actual del tablero de juego.
         */
        public Arbol_N_Ario(byte columnas, byte filas, int conecta, byte[][] estado, byte profundidad){
            _columnas = columnas;
            _filas = filas;
            _conecta = conecta;
            _raiz = new Nodo(estado);
            long startTime = System.nanoTime();
            solucionParcial = rellenaArbol(profundidad);
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println("Execution time in seconds: " + timeElapsed / 1000000000);
        }

        /**
         * @brief Comprueba si para un cierto caso alguno de los dos competidores gana.
         * @param tablero Caso a comprobar
         * @return 1 si gana el jugador, -1 si gana el agente inteligente, 0 en caso de que ninguno gane.
         */
        private int checkWin(byte[][] tablero) {

            int ganador = 0;
            int ganar1;
            int ganar2;
            boolean salir = false;
            // Comprobar horizontal
            for (int i = 0; (i < _filas) && !salir; i++) {
                ganar1 = 0;
                ganar2 = 0;
                for (int j = 0; (j < _columnas) && !salir; j++) {
                    if (tablero[i][j] != Main.VACIO) {
                        if (tablero[i][j] == Main.PLAYER1) {
                            ganar1++;
                        } else {
                            ganar1 = 0;
                        }
                        // Gana el jugador 1
                        if (ganar1 >= _conecta) {
                            ganador = Main.PLAYER1;
                            // Ganador 1 en horizontal;
                            salir = true;
                        }
                        if (!salir) {
                            if (tablero[i][j] == Main.PLAYER2) {
                                ganar2++;
                            } else {
                                ganar2 = 0;
                            }
                            // Gana el jugador 2
                            if (ganar2 >= _conecta) {
                                ganador = Main.PLAYER2;
                                // Ganador 2 en horizontal;
                                salir = true;
                            }
                        }
                    } else {
                        ganar1 = 0;
                        ganar2 = 0;
                    }
                }
            }
            // Comprobar vertical
            for (int i = 0; (i < _columnas) && !salir; i++) {
                ganar1 = 0;
                ganar2 = 0;
                for (int j = 0; (j < _filas) && !salir; j++) {
                    if (tablero[j][i] != Main.VACIO) {
                        if (tablero[j][i] == Main.PLAYER1) {
                            ganar1++;
                        } else {
                            ganar1 = 0;
                        }
                        // Gana el jugador 1
                        if (ganar1 >= _conecta) {
                            ganador = Main.PLAYER1;
                            // Ganador 1 en vertical;
                            salir = true;
                        }
                        if (!salir) {
                            if (tablero[j][i] == Main.PLAYER2) {
                                ganar2++;
                            } else {
                                ganar2 = 0;
                            }
                            // Gana el jugador 2
                            if (ganar2 >= _conecta) {
                                ganador = Main.PLAYER2;
                                // Ganador 2 en vertical;
                                salir = true;
                            }
                        }
                    } else {
                        ganar1 = 0;
                        ganar2 = 0;
                    }
                }
            }
            // Comprobar oblicuo. De izquierda a derecha
            for (int i = 0; i < _filas && !salir; i++) {
                for (int j = 0; j < _columnas && !salir; j++) {
                    int a = i;
                    int b = j;
                    ganar1 = 0;
                    ganar2 = 0;
                    while (a < _filas && b < _columnas && !salir) {
                        if (tablero[a][b] != Main.VACIO) {
                            if (tablero[a][b] == Main.PLAYER1) {
                                ganar1++;
                            } else {
                                ganar1 = 0;
                            }
                            // Gana el jugador 1
                            if (ganar1 >= _conecta) {
                                ganador = Main.PLAYER1;
                                // Ganador 1 en oblicuo izquierda;
                                salir = true;
                            }
                            if (ganador != Main.PLAYER1) {
                                if (tablero[a][b] == Main.PLAYER2) {
                                    ganar2++;
                                } else {
                                    ganar2 = 0;
                                }
                                // Gana el jugador 2
                                if (ganar2 >= _conecta) {
                                    ganador = Main.PLAYER2;
                                    // Ganador 2 en oblicuo izquierda;
                                    salir = true;
                                }
                            }
                        } else {
                            ganar1 = 0;
                            ganar2 = 0;
                        }
                        a++;
                        b++;
                    }
                }
            }
            // Comprobar oblicuo de derecha a izquierda
            for (int i = _filas - 1; i >= 0 && !salir; i--) {
                for (int j = 0; j < _columnas && !salir; j++) {
                    int a = i;
                    int b = j;
                    ganar1 = 0;
                    ganar2 = 0;
                    while (a >= 0 && b < _columnas && !salir) {
                        if (tablero[a][b] != Main.VACIO) {
                            if (tablero[a][b] == Main.PLAYER1) {
                                ganar1++;
                            } else {
                                ganar1 = 0;
                            }
                            // Gana el jugador 1
                            if (ganar1 >= _conecta) {
                                ganador = Main.PLAYER1;
                                // Ganador 1 en oblicuo derecha
                                salir = true;
                            }
                            if (ganador != Main.PLAYER1) {
                                if (tablero[a][b] == Main.PLAYER2) {
                                    ganar2++;
                                } else {
                                    ganar2 = 0;
                                }
                                // Gana el jugador 2
                                if (ganar2 >= _conecta) {
                                    ganador = Main.PLAYER2;
                                    // Ganador 2 en oblicuo derecha
                                    salir = true;
                                }
                            }
                        } else {
                            ganar1 = 0;
                            ganar2 = 0;
                        }
                        a--;
                        b++;
                    }
                }
            }

            return ganador;
        }

        /*private void showMatrix(int[][] m){
            for (int i = 0; i < _columnas; i++) {
                for (int j = 0; j < _filas; j++) {
                    System.out.print(m[j][i]);
                    System.out.print("  ");
                }
                System.out.println("");
            }
        }*/

        /**
         * @brief Comprueba en cuantos huecos se puede poner en un turno dado un caso de tablero concreto.
         * @param m Caso concreto de tablero.
         * @return matriz donde el (n,0) será la posicion x de un posivle movimiento y las (n,1) la posicion y del mismo.
         */
        private int[][] posiblesHuecos(byte[][] m){
            int n = _columnas;
            for (int i = 0; i < _columnas; ++i) {
                if(m[0][i]!=0){
                    n -= 1;
                }
            }
            if(n==0 || checkWin(m)!=0){
                return null;
            }
            int[][] aux = new int[n][2];
            int cont = 0;
            for (int i = 0; i < _columnas; i++) {
                for (int j = _filas-1; j >= 0; j--) {
                    if (m[j][i] == 0) {
                        aux[cont][0] = i;
                        aux[cont][1] = j;
                        cont++;
                        break;
                    }
                }
            }
            return aux;
        }

        /**
         * @brief Devuelve la copia del estado de un nodo suministrado.
         * @param nodo nodo que contiene el estado a copiar.
         * @return copia del atributo estado del nodo.
         */
        private byte[][] copiaEstado(Nodo nodo){
            byte[][] copia = new byte[_filas][_columnas];
            for (int j = 0; j < _filas; j++) {
                System.arraycopy(nodo._estado[j], 0, copia[j], 0, _columnas);
            }
            return copia;
        }

        /**
         * @brief Este método se encarga de ir rellenando el árbol con los diferentes casos posibles. Si encuentra una
         * rama en la que el agente gane (-1), devolverá la columna en la que hay que poner. En cualquier otro caso
         * se calcularán todas las posibilidades y devolverá uno de los mejores casos posibles.
         * @param profundidad profundidad a la que se encuentra el nodo (número de movimientos anteriores).
         * @return columna en la habrá que poner.
         */
        private byte rellenaArbol(byte profundidad){
            _raiz.profundidad = profundidad;
            //rellena(_raiz, true, profundidad);
            profundidad++;
            int[][] vAux = posiblesHuecos(_raiz._estado);
            Nodo auxNodo = null;
            if(vAux!=null) {
                for (byte i = 0; i < vAux.length; i++) {
                    Nodo aux = new Nodo(_raiz._estado);
                    aux._estado = copiaEstado(_raiz);
                    aux._estado[vAux[i][1]][vAux[i][0]] = -1;
                    Nodo nodoAux = buscaNodoProfundidad(aux, profundidad);
                    if (nodoAux != null) {
                        _raiz.insertarHijo(nodoAux);
                    } else {
                        _raiz.insertarHijo(aux);
                        _raiz._hijos.get(i).profundidad = profundidad;
                        _raiz._hijos.get(i).columna = (byte) vAux[i][0];
                    }
                }
                for (int i = 0; i < vAux.length; i++) {
                    int auxInt;
                    rellena(_raiz._hijos.get(i), false, profundidad);
                    calculaValores(_raiz._hijos.get(i));
                    auxInt = recorridoInorden(_raiz._hijos.get(i));
                    if(auxInt == -1){
                        return _raiz._hijos.get(i).columna;
                    }else if (auxInt == 0){
                        auxNodo = _raiz._hijos.get(i);
                    }
                }
                arbolCompleto = true;
                if(auxNodo == null){
                    return _raiz._hijos.get(0).columna;
                }
                return auxNodo.columna;
            }
            return -1;
        }

        /**
         * @brief Se encarga de rellenar el arbol de manera recursiva con las distintas posibilidades que hay.
         * @param nodo nodo suministrado para comprobar sus hijos.
         * @param turno true si es su turno.
         */
        private void rellena(Nodo nodo, boolean turno, byte profundidad){
            profundidad++;
            int[][] vAux = posiblesHuecos(nodo._estado);
            if(vAux!=null) {
                for (byte i = 0; i < vAux.length; i++) {
                    Nodo aux = new Nodo(nodo._estado);
                    aux._estado = copiaEstado(nodo);
                    if (turno) {
                        aux._estado[vAux[i][1]][vAux[i][0]] = -1;
                    } else {
                        aux._estado[vAux[i][1]][vAux[i][0]] = 1;
                    }
                    Nodo nodoAux = buscaNodoProfundidad(aux, profundidad);
                    if (nodoAux != null) {
                        nodo.insertarHijo(nodoAux);
                    } else {
                        nodo.insertarHijo(aux);
                        nodo._hijos.get(i).profundidad = profundidad;
                        nodo._hijos.get(i).columna = (byte) vAux[i][0];
                    }
                }
                for (int i = 0; i < vAux.length; i++) {
                    rellena(nodo._hijos.get(i), !turno, profundidad);
                }
            }
        }

        /**
         * @brief Busca un nodo que cuelgue de la raiz.
         * @param nodo Nodo a buscar.
         * @return Nodo buscado en caso de encontrarlo o null en caso de que no.
         */
        private Nodo buscaNodo(Nodo nodo){
            if(_raiz._estado==nodo._estado){
                return _raiz;
            }
            return buscaNodo(nodo, _raiz);
        }

        /**
         * @brief Busca un nodo que cuelgue de otro sumunistrado.
         * @param nBuscado Nodo buscado.
         * @param nSuministrado Nodo suministrado del que debe de colgar el nodo buscado.
         * @return Nodo buscado si lo encuentra o null en caso de no encontrarlo.
         */
        private Nodo buscaNodo(Nodo nBuscado, Nodo nSuministrado) {
            for (int i=0;i<nSuministrado._hijos.size(); i++){
                if(nBuscado._estado==nSuministrado._hijos.get(i)._estado){
                    return nSuministrado._hijos.get(i);
                }
            }
            for (int i=0; i<nSuministrado._hijos.size(); i++) {
                Nodo v = buscaNodo(nBuscado, nSuministrado._hijos.get(i));
                if (v != null) {
                    return v;
                }
            }
            return null;
        }

        /**
         * @brief Busca un nodo que está a una profundidad determinada.
         * @param nodo Podo a buscar.
         * @param profunidad Profundidad a la que debe estar.
         * @return Nodo buscado si se encuentra o null en caso contrario.
         */
        private Nodo buscaNodoProfundidad(Nodo nodo, byte profunidad) {
            if(_raiz.equals(nodo)) {
                return nodo;
            }else {
                return buscaNodoProfundidadR(_raiz, nodo, profunidad);
            }
        }

        /**
         * @brief Busca un nodo que está a una profundidad determinada que sea hijo del nodo suministrado.
         * @param nSuministrado Nodo del que cuelga el nodo a buscar.
         * @param nodo Nodo a buscar.
         * @param profunidad Profundidad a la que debe estar.
         * @return Nodo buscado si se encuentra o null en caso contrario.
         */
        private Nodo buscaNodoProfundidadR(Nodo nSuministrado, Nodo nodo, byte profunidad) {
            if (nSuministrado.valor == profunidad-1) {
                for (int i = 0; i < nSuministrado._hijos.size(); i++) {
                    boolean comprobar = true;
                    for (int j = 0; j < _filas; j++) {
                        for (int k = 0; k < _columnas; k++) {
                            if (nSuministrado._estado[j][k]!=nodo._estado[j][k]){
                                comprobar = false;
                            }
                        }
                    }
                    if(comprobar){
                        return nSuministrado._hijos.get(i);
                    }
                }
                return null;
            }
            for (int i = 0; i < nSuministrado._hijos.size(); i++) {
                Nodo aux = buscaNodoProfundidadR(nSuministrado._hijos.get(i), nodo, profunidad);
                if(aux!=null){
                    return aux;
                }
            }
            return null;
        }

        /**
         * @brief Busca un nodo que tenga un estado igual al sumunistrado y que cuelgue de la raiz.
         * @param estado Estado a buscar.
         * @return Nodo buscado en caso de encontrarlo o null en caso de que no.
         */
        private Nodo buscaEstado(byte[][] estado){
            if(_raiz._estado==estado){
                return _raiz;
            }
            return buscaEstado(estado, _raiz, 1);
        }

        /**
         * @brief Busca un nodo que tenga un estado igual al sumunistrado y que cuelgue del nodo suministrado.
         * @param estado Estado a buscar.
         * @param nSuministrado Nodo suministrado del que debe de colgar el nodo buscado.
         * @param profundidad profundidad a la que se encontrará como máximo el estado a buscar.
         * @return Nodo buscado si lo encuentra o null en caso de no encontrarlo.
         */
        private Nodo buscaEstado(byte[][] estado, Nodo nSuministrado, int profundidad) {
            for (int i=0;i<nSuministrado._hijos.size(); i++){
                boolean v = true;
                for (int j = 0; j < _filas; j++) {
                    for (int k = 0; k < _columnas; k++) {
                        if(estado[j][k]!=nSuministrado._hijos.get(i)._estado[j][k]){
                            v=false;
                        }
                    }
                }
                if(v){
                    return nSuministrado._hijos.get(i);
                }
            }
            if(profundidad!=0) {
                for (int i = 0; i < nSuministrado._hijos.size(); i++) {
                    Nodo v = buscaEstado(estado, nSuministrado._hijos.get(i), profundidad - 1);
                    if (v != null) {
                        return v;
                    }
                }
            }
            return null;
        }

        /**
         * @brief Calcula y modifica la variable valores de la raiz y de sus hijos con los siguientes datos:
         * · -1: si en el nodo encontrado gana el agente inteligente.
         * · 1: si en el nodo encontrado gana el jugador.
         * · No se modifica: se queda a 0 en caso de empate o caso de paso.
         */
        /*private void calculaValores(){
            calculaValores(_raiz);
        }*/

        /**
         * @brief Calcula y modifica la variable valores del nodo suministrado y de sus hijos con los siguientes datos:
         * · -1: si en el nodo encontrado gana el agente inteligente.
         * · 1: si en el nodo encontrado gana el jugador.
         * · No se modifica: se queda a 0 en caso de empate o caso de paso.
         * @param nodo nodo desde el que se hacen las comprobaciones.
         */
        private void calculaValores(Nodo nodo){
            if(nodo._hijos.isEmpty()){
                nodo.valor = (byte) checkWin(nodo._estado);
            } else {
                for (int i = 0; i < nodo._hijos.size(); i++) {
                    calculaValores(nodo._hijos.get(i));
                }
            }
        }

        /**
         * @brief mueve el parametro raiz a uno de sus hijos actualizando así el caso actual.
         * @param m Matriz con el caso actual.
         */
        public void actualizar(byte[][] m){

            _raiz = buscaEstado(m);
        }

        /**
         * @brief Recorre en inorden el subárbol que tiene como raíz el nodo suministrado
         * y según el turno devuelve el valor que más le interesaría al jugador pertinente.
         * @param nodo nodo raíz del subárbol.
         * @return -1 en caso de que por ese camino el agente se asegure la victoria, 0 en caso de empate asegurado,
         * 1 en caso de perdida asegurada.
         */
         public byte recorridoInorden(Nodo nodo) {
            byte ganar=0, empatar=0, aux = 0;
            if(nodo._hijos.isEmpty()) {
                return nodo.valor;
            } else {
                if (turno%2==0) {
                    for (int i = 0; i < nodo._hijos.size(); i++) {
                        aux = recorridoInorden(nodo._hijos.get(i));
                        if (aux==0){
                            empatar++;
                        }
                        if (aux==-1){
                            return -1;
                        }
                    }
                    if(empatar>0){
                        return 0;
                    }
                    return 1;
                } else {
                    for (int i = 0; i < nodo._hijos.size(); i++) {
                        aux = recorridoInorden(nodo._hijos.get(i));
                        if (aux==0){
                            empatar++;
                        }
                        if (aux==1){
                            return 1;
                        }
                    }
                    if(empatar>0){
                        return 0;
                    }
                    return -1;
                }
            }
        }

        /**
         * @brief calcula si en el siguiente movimiento gana alguno de los jugadores. En tal caso coloca para ganar o
         * para tapar al adversario.
         * @return byte con la columna en la que poner o -1 si nadie gana en el siguiente movimiento.
         */
         public byte isWin(){
             for (int i = 0; i < _raiz._hijos.size(); i++) {
                 if (checkWin(_raiz._hijos.get(i)._estado)==-1){
                     return _raiz._hijos.get(i).columna;
                 }
             }
             for (int i = 0; i < _raiz._hijos.size(); i++) {
                 for (int j = 0; j < _raiz._hijos.get(i)._hijos.size(); j++) {
                     if(checkWin(_raiz._hijos.get(i)._hijos.get(j)._estado)==1){
                         return _raiz._hijos.get(i)._hijos.get(j).columna;
                     }
                 }
             }

             return -1;
        }
    }

    private byte solucionParcial = -1;
    private byte turno = 0; /// Variable usada para hacer cosas única y exclusivamente el primer turno.
    private Arbol_N_Ario arbol = null;/// Declaración del árbol.

    /**
     * @brief Comprobación si se ha puesto (si el número de columnas es impar) en el primer espacio de la columna central.
     * En caso de que el usuario no haya puesto en la casilla central se pondrá en la ella la primera ficha.
     * @param tablero
     * @return movimiento a hacer o -1 si no exite o no se puede poner en la pimera casilla de la columna central.
     */
    private int turno1 (Grid tablero) {
        if (tablero.getColumnas()%2!=0){
            if (tablero.get(tablero.getFilas()-1, (tablero.getColumnas()-1)/2) == 0) {
                return tablero.getColumnas()/2;
            }
        }

        return -1;
    }

    /**
     * @brief funcion que determina donde colocar la ficha este turno
     * @param tablero Tablero de juego
     * @param conecta Número de fichas consecutivas adyacentes necesarias para
     * ganar
     * @return Devuelve si ha ganado algun jugador
     */
    @Override
    public int turno(Grid tablero, int conecta) {
        int posicion;

        // Comprobación del primer movimiento a hacer y creación del árbol de posibilidades.
        if (turno == 0) {
            if(conecta==4 && tablero.getColumnas()==7 && tablero.getFilas()==6) {
                turno++;
                posicion = turno1(tablero);
                if (posicion != -1) {
                    return posicion;
                } else {
                    arbol = new Arbol_N_Ario((byte) tablero.getColumnas(), (byte) tablero.getFilas(), conecta, byteToInt(tablero.copyGrid(), tablero.getFilas(), tablero.getColumnas()), (byte) 1);
                    turno++;
                }
            } else {
                arbol = new Arbol_N_Ario((byte) tablero.getColumnas(), (byte) tablero.getFilas(), conecta, byteToInt(tablero.copyGrid(), tablero.getFilas(), tablero.getColumnas()), (byte)3);
                turno+=2;
                if (!arbol.arbolCompleto){
                    return solucionParcial;
                }
            }
        }else if (turno==1) {
            turno+=2;
            if (!arbol.arbolCompleto) {
                arbol = new Arbol_N_Ario((byte) tablero.getColumnas(), (byte) tablero.getFilas(), conecta, byteToInt(tablero.copyGrid(), tablero.getFilas(), tablero.getColumnas()), (byte) 3);
                return solucionParcial;
            }
        } else if (!arbol.arbolCompleto){
            byte auxProf = (byte) (arbol._raiz.profundidad+2);
            arbol = new Arbol_N_Ario((byte) tablero.getColumnas(), (byte) tablero.getFilas(), conecta, byteToInt(tablero.copyGrid(), tablero.getFilas(), tablero.getColumnas()), auxProf);
            byte win = arbol.isWin();
            return (win==-1) ? solucionParcial : win;
        } else {
            arbol.actualizar(byteToInt(tablero.getGrid(), tablero.getFilas(), tablero.getColumnas()));
        }

        posicion = getmejorOpcion();

        return posicion;

    } // turno

    /**
     * @brief Cambia el tipo de una matriz de enteros a bytes.
     * @param m Matriz de enteros a cambiar.
     * @param fila Número de filas.
     * @param columna Número de columnas.
     * @return la matriz con datos tipo byte en vez ve tipo int.
     */
    private byte[][] byteToInt (int[][] m, int fila, int columna){
        byte[][] bytes = new byte[fila][columna];
        for (int i = 0; i < fila; i++) {
            for (int j = 0; j < columna; j++) {
                bytes[i][j]=(byte) m[i][j];
            }
        }
        return bytes;
    }

    /**
     * @brief Devuelve en que columna es mejor poner la ficha.
     * @return entero con la columna.
     */
    private int getmejorOpcion(){
        byte[] mejor = new byte[2];
        byte candidato;
        byte win = arbol.isWin();
        if (win!=-1){
            return win;
        }
        mejor[0] = arbol.recorridoInorden(arbol._raiz._hijos.get(0));
        mejor[1] = arbol._raiz._hijos.get(0).columna;
        for (int i = 0; i < arbol._raiz._hijos.size(); i++) {
            if (mejor[0]==-1){
                return mejor[1];
            }
            candidato = arbol.recorridoInorden(arbol._raiz._hijos.get(i));
            if(candidato<mejor[0]){
                mejor[0] = arbol.recorridoInorden(arbol._raiz._hijos.get(i));
                mejor[1] = arbol._raiz._hijos.get(i).columna;
            }
        }

        return mejor[1];
    }

} // MiniMaxPlayer
