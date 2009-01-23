1-Requerimientos: 
	Para ejecutar: jre 1.5 o superior
    Para compilar: jdk 1.5 o superior

MiniPascal se compilo en j2sdk1.5.0_06	

Dentro del directorio “dist” se encuentra el archivo MiniPascal.jar 
que contiene todas las clases necesarias para la ejecución del analizador 
léxico y sintactico, también para los test. 

Nota: las versiones anteriores a la 1.5 no soportan genericidad (templates)
ni tampoco otras caracteristicas usadas en este proyecto.
Podes bajar la ultima version desde: http://java.sun.com/

2- ¿Como ejecutar el compilador?

  Antes que nada, asegurate de tener seteada la variable PATH 
  apuntando al directorio donde tenes los ejecutables del jdk.
  Ejemplo: 
     set PATH=%PATH%;c:\ruta\jdk\bin (windows)
	 export PATH=$PATH:/ruta/../jdk/bin (Unix/Linux)
	 
  Luego desde la linea de comandos:
  java -cp dist/MiniPascal.jar main.Main archivo.pas
  
  Si solamente deseamos compilar sin generar codigo,
  cambiar el valor gen_codigo a off dentro del archivo
  de configuracion: minipas.conf
  
  
Nota: en el punto 4 explico como setear las variables de entorno
para que queden definidas para siempre.

3- ¿Como correr una bateria de test?

   Hay una clase llamada Testeador, que permite testear uno o varios archivos
   simultaneamente a partir de un directorio dado.
   Su sintaxis es:
   java -jar dist/MiniPascal.jar <clase testeable> <path o archivo>

Ejemplos:   
    java -jar dist/MiniPascal.jar aLexico.AnalizadorLexico  <file> (testea un unico archivo)
    java -jar dist/MiniPascal.jar aSintactico.AnalizadorSintactico (testea desde el directorio actual)
    java -jar dist/MiniPascal.jar mepa.ASMepa  <path> (testea a partir del path dado)
    java -jar dist/MiniPascal.jar mepa.ALMepa  <file> (testea un unico archivo)

   Podemos redirigir la salida a un archivo en lugar de pantalla. Ejemplo:
   java -jar dist/MiniPascal.jar aSintactico.AnalizadorSintactico <file> > result.txt
   Esto es muy util cuando testeamos muchos archivos.
   
4- ¿Como dejo seteadas las variables de entorno definitivamente?

Windows98:
	Edita el archivo c:\autoexec.bat y agrega la linea del punto 1-
Nota: es necesario reiniciar para que los cambios tengan efecto.

WindowsXP/2000/2003:
	Doble click en MiPC->propiedades->opciones avanzadas->variables de entorno.
	En esta ventana podes editar la variable PATH agregando al final la sig cadena:
	;c:\ruta\jdk\bin
	Tambien podemos agregar nuevas variables. Por ejemplo
	JAVA_HOME, con el valor C:\ruta\jdk

Unix/Linux:
    Edita el archivo .bashrc que se encuentra en el home del usuario
	#cd ~
	#vi .bashrc
	Agrega la linea del punto 1), 
	export PATH=$PATH:/ruta/../jdk/bin (Linux)
	Luego guardas y salis. 
Nota: Solo basta con abrir una nueva terminal para obtener los nuevos cambios.

5- JAVADOC
    En la carpeta dist/javadoc/ se encuentra la documentacion de las API's

	
Cualquier duda o comentario enviame un e-mail a eabett@yahoo.com.ar
Suerte!	