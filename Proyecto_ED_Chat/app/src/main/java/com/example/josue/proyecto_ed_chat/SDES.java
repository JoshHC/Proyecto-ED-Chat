package com.example.josue.proyecto_ed_chat;

public class SDES {

    static String [][] S0 = {{"01","00","11","10"},{"11","10","01","00"},{"00","10","01","11"}, {"11","01","11","10"}};
    static String [][] S1 = {{"00","01","10","11"},{"10","00","01","11"},{"11","00","01","00"}, {"10","01","00","11"}};
    private static int[] K1 = new int [8];
    private static int[] K2 = new int [8];


    public String Cifrar(String Texto, String Clave)
    {
        char[] cadenaChar;
        cadenaChar = Clave.toCharArray();
        int[] cadenaInt = new int[10];

        for (int i = 0; i < 10; i++)
        {
            cadenaInt[i] = cadenaChar[i];

            //48 porque char 0 es int 48
            //49 porque char 1 es int 49
            if(cadenaInt[i] == 48)
                cadenaInt[i] = 0;
            else
                cadenaInt[i] = 1;
        }

        // Se generan la llaves con la cadena ingresada por el usuario
        GenerarLlaves(cadenaInt);

        char[] TextoaCifrar = Texto.toCharArray();
        String Salida = "";

        // Cifra caracter por caracter
        for(int i = 0; i < TextoaCifrar.length; i++)
        {
            char letraCifrada = CifrarSDES(TextoaCifrar[i]);
            Salida += String.valueOf(letraCifrada);
        }

        return Salida;

    }

    public String Descifrar(String Cifrado, String Clave)
    {
        char[] cadenaChar;
        cadenaChar = Clave.toCharArray();
        int[] cadenaInt = new int[10];

        for (int i = 0; i < 10; i++)
        {
            cadenaInt[i] = cadenaChar[i];

            //48 porque char 0 es int 48
            //49 porque char 1 es int 49
            if(cadenaInt[i] == 48)
                cadenaInt[i] = 0;
            else
                cadenaInt[i] = 1;
        }

        // Se generan la llaves con la cadena ingresada por el usuario
        GenerarLlaves(cadenaInt);

        char[] TextoaDescifrar = Cifrado.toCharArray();
        String Salida = "";

        // Cifra caracter por caracter
        for(int i = 0; i < TextoaDescifrar.length; i++)
        {
            char letraCifrada = CifrarSDES(TextoaDescifrar[i]);
            Salida += String.valueOf(letraCifrada);
        }

        return Salida;

    }


    private static int[] RellenarCeros(char Letra) {
        int numero = Letra;
        String Aux = Integer.toBinaryString(numero);
        char[] Arrayderelleno = Aux.toCharArray();
        int[] ArrayRellenado = new int[8];

        if (Arrayderelleno.length < 8) {

            int Cantidad = 8 - Aux.length();
            String ceros = "";

            for (int i = 0; i < Cantidad; i++) {
                ceros = ceros + "0";
            }

            Aux = ceros + Aux;
            Arrayderelleno = Aux.toCharArray();

            for (int i = 0; i < Arrayderelleno.length; i++) {
                ArrayRellenado[i] = Arrayderelleno[i];

                if (ArrayRellenado[i] == 48)
                    ArrayRellenado[i] = 0;
                else
                    ArrayRellenado[i] = 1;
            }

        } else {
            for (int i = 0; i < Arrayderelleno.length; i++) {
                ArrayRellenado[i] = Arrayderelleno[i];

                if (ArrayRellenado[i] == 48)
                    ArrayRellenado[i] = 0;
                else
                    ArrayRellenado[i] = 1;
            }
        }


        return ArrayRellenado;

    }


    private static void GenerarLlaves(int [] Entrada)
    {
        int[] temp = new int[10];
        temp = P10(Entrada);

        int[] Parte1 = new int[5];
        int[] Parte2 = new int[5];

        for (int i = 0; i < 5; i++)
        {
            Parte1[i] = temp[i];
            Parte2[i] = temp[i+5];
        }

        Parte1 = LS1(Parte1);
        Parte2 = LS1(Parte2);

        for(int i = 0 ; i < 5; i++)
            temp[i] = Parte1[i];

        for(int i = 5; i < 10; i++)
            temp[i] = Parte2[i-5];

        K1 = P8(temp);

        Parte1 = LS2(Parte1);
        Parte2 = LS2(Parte2);

        for(int i = 0 ; i < 5; i++)
            temp[i] = Parte1[i];

        for(int i = 5; i < 10; i++)
            temp[i] = Parte2[i-5];

        K2 = P8(temp);
    }


    private static char CifrarSDES(char Letra)
    {
        int[] temp = new int[8];
        temp = RellenarCeros(Letra);

        temp = PInicial(temp);

        int[] Externo1 = new int[4];
        int[] Externo2 = new int[4];

        for (int i = 0; i < 4; i++)
        {
            Externo1[i] = temp[i];
            Externo2[i] = temp[i+4];
        }

        int[] TempInterno = new int[8];

        TempInterno = EP(Externo2);
        TempInterno = XOR(TempInterno, K1);

        int[] SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo1 = XOR(Externo1, SBox);

        // SE DAN VUELTA EXTERNO1 Y EXTERNO2 (PRESENTACION)

        temp = new int[8];
        TempInterno = new int[8];

        TempInterno = EP(Externo1);
        TempInterno = XOR(TempInterno, K2);

        SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo2 = XOR(Externo2, SBox);

        for (int i = 0; i < 4; i++)
            temp[i] = Externo2[i];

        for (int i = 4; i < 8; i++)
            temp[i] = Externo1[i-4];

        int[] cifrado = new int[8];
        cifrado = IPInverso(temp);

        String texto = "";

        for (int i = 0; i < 8; i++)
            texto += cifrado[i];

        int numero = Integer.parseInt(texto,2);
        char letraCifrado = (char) numero;

        return letraCifrado;
    }

    private static char DescifrarSDES(char Letra)
    {
        int[] temp = new int[8];
        temp = RellenarCeros(Letra);

        temp = PInicial(temp);

        int[] Externo1 = new int[4];
        int[] Externo2 = new int[4];

        for (int i = 0; i < 4; i++)
        {
            Externo1[i] = temp[i];
            Externo2[i] = temp[i+4];
        }

        int[] TempInterno = new int[8];

        TempInterno = EP(Externo2);
        TempInterno = XOR(TempInterno, K2);

        int[] SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo1 = XOR(Externo1, SBox);

        // SE DAN VUELTA EXTERNO1 Y EXTERNO2 (PRESENTACION)

        temp = new int[8];
        TempInterno = new int[8];

        TempInterno = EP(Externo1);
        TempInterno = XOR(TempInterno, K1);

        SBox = new int[4];

        SBox = SBoxes(TempInterno);
        SBox = P4(SBox);

        Externo2 = XOR(Externo2, SBox);

        for (int i = 0; i < 4; i++)
            temp[i] = Externo2[i];

        for (int i = 4; i < 8; i++)
            temp[i] = Externo1[i-4];

        int[] cifrado = new int[8];
        cifrado = IPInverso(temp);

        String texto = "";

        for (int i = 0; i < 8; i++)
            texto += cifrado[i];

        int numero = Integer.parseInt(texto,2);
        char letraDescifrado = (char) numero;

        return letraDescifrado;
    }

    //PROCESO
    private static int[] LS1(int[] Cadena)
    {
        int temp = Cadena[0];

        for (int i = 0; i < 4; i++)
            Cadena[i] = Cadena[i+1];

        Cadena[4] = temp;

        return Cadena;
    }

    //PROCESO
    private static int[] LS2(int[] Cadena)
    {
        int temp1 = Cadena[0];
        int temp2 = Cadena[1];

        for (int i = 0; i < 3; i++)
            Cadena[i] = Cadena[i+2];

        Cadena[3] = temp1;
        Cadena[4] = temp2;

        return Cadena;
    }

    //LLAVES
    private static int[] IPInverso(int[] Entrada)
    {
        ConstantesSDES Datos = new ConstantesSDES();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i < 8; i++)
        {
            ArregloAuxiliar[Datos.PermutacionInicial[i]] = Entrada[i];
        }

        return ArregloAuxiliar;
    }

    //PROCESO
    private static int[] XOR (int[] Comparador1, int[] Comparador2)
    {
        int[] XOR = new int[Comparador1.length];

        for (int i = 0; i < Comparador1.length; i++)
        {
            if(Comparador1[i] == Comparador2[i])
                XOR[i] = 0;
            else
                XOR[i] = 1;
        }

        return XOR;
    }

    //PROCESO
    private static int[] SBoxes(int [] Arreglo)
    {
        String Cadena;
        int[] Auxiliar = new int[4];
        String [] S0box = new String [2];
        String [] S1box = new String [2];

        String Aux = "";

        Aux = String.valueOf(Arreglo[0]);
        Aux = Aux + String.valueOf(Arreglo[3]);
        S0box[0] = Aux;
        Aux = "";
        Aux = String.valueOf(Arreglo[1]);
        Aux = Aux + String.valueOf(Arreglo[2]);
        S0box[1] = Aux;

        Aux = String.valueOf(Arreglo[4]);
        Aux = Aux + String.valueOf(Arreglo[7]);
        S1box[0] = Aux;
        Aux = "";
        Aux = String.valueOf(Arreglo[5]);
        Aux = Aux + String.valueOf(Arreglo[6]);
        S1box[1] = Aux;


        int Fila = Integer.valueOf(S0box[0],2);
        int Columna = Integer.valueOf(S0box[1],2);

        Cadena = S0[Fila][Columna];

        Fila = Integer.valueOf(S1box[0],2);
        Columna = Integer.valueOf(S1box[1],2);

        Cadena = Cadena + S1[Fila][Columna];

        char[] cadenaSeparada = Cadena.toCharArray();
        for (int i = 0; i < cadenaSeparada.length; i++)
        {
            Auxiliar[i] = cadenaSeparada[i];

            if(Auxiliar[i] == 48)
                Auxiliar[i] = 0;
            else
                Auxiliar[i] = 1;
        }

        return Auxiliar;
    }

    //PROCESO
    public String BinarioaString(int [] texto)
    {

        String Auxiliar = texto.toString();
        int TextoBinario = Integer.valueOf(Auxiliar,2);
        String Letra = String.valueOf((char) TextoBinario);

        return Letra;
    }

    //LLAVE
    private static int[] PInicial(int[] Entrada)
    {
        ConstantesSDES Datos = new ConstantesSDES();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i< Datos.PermutacionInicial.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.PermutacionInicial[i]];
        }

        return ArregloAuxiliar;
    }

    //LLAVE
    private static int[] P10(int[] Entrada)
    {
        ConstantesSDES Datos = new ConstantesSDES();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i< Datos.P10.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.P10[i]];
        }

        return ArregloAuxiliar;
    }

    //LLAVE
    private static int[] P8(int[] Entrada)
    {
        ConstantesSDES Datos = new ConstantesSDES();
        int [] ArregloAuxiliar = new int[Datos.P8.length];


        for(int i = 0; i< Datos.P8.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.P8[i]];
        }

        return ArregloAuxiliar;
    }

    //LLAVE
    private static int [] P4(int[] Entrada)
    {
        ConstantesSDES Datos = new ConstantesSDES();
        int [] ArregloAuxiliar = new int[Entrada.length];


        for(int i = 0; i< Datos.P4.length; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.P4[i]];
        }

        return ArregloAuxiliar;
    }

    //LLAVE
    private static int[] EP(int[] Entrada)
    {
        ConstantesSDES Datos = new ConstantesSDES();
        int [] ArregloAuxiliar = new int[Datos.EP.length];


        for(int i = 0; i<3; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.EP[i]];
        }

        for(int i = 3; i<=7; i++)
        {
            ArregloAuxiliar[i] = Entrada[Datos.EP[i]];
        }

        return ArregloAuxiliar;
    }


}

