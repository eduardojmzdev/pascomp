1-Requerimientos: 
    Para ejecutar: jre 1.6 o superior
    Para compilar: jdk 1.6 o superior
    


Dentro del directorio “dist” se encuentra el archivo plg0809-grupo8.jar 
que contiene todas las clases necesarias para la ejecución del analizador 
léxico y sintactico, también para los tests. 


2- ¿Cómo ejecutar el compilador?

  Teniendo una máquina virtual de java 1.6 o superior en ruta, desde la linea de comandos:
  java -cp dist/plg0809-grupo8.jar main.Main archivo.pas
  
  Si solamente deseamos compilar sin generar codigo,
  cambiar el valor gen_codigo a off dentro del archivo
  de configuracion: minipas.conf
  
  
3- ¿Cómo ejecutar una bateria de tests?

   Hay una clase llamada Testeador, que permite testear uno o varios archivos
   simultaneamente a partir de un directorio dado.
   Su sintaxis es:
   java -jar dist/plg0809-grupo8.jar <clase testeable> <path o archivo>

Ejemplos:   
    java -jar dist/MiniPascal.jar aLexico.AnalizadorLexico  <file> (testea un unico archivo)
    java -jar dist/MiniPascal.jar aSintactico.AnalizadorSintactico (testea desde el directorio actual)
    java -jar dist/MiniPascal.jar mepa.ASMepa  <path> (testea a partir del path dado)
    java -jar dist/MiniPascal.jar mepa.ALMepa  <file> (testea un unico archivo)

   Podemos redirigir la salida a un archivo en lugar de pantalla. Ejemplo:
   java -jar dist/MiniPascal.jar aSintactico.AnalizadorSintactico <file> > result.txt
   Esto es muy util cuando testeamos muchos archivos.
   