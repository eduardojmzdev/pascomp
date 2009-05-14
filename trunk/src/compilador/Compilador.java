/**
 * Paquete que recoge los archivos que constituyen la interfaz 
 * gráfica del compilador.
 */
package compilador;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import aSintactico.*;

import mVirtual.*;
import tablaSimbolos.*;
import utils.TPasoApaso;

/**
 * Clase que implementa una GUI para usar el compilador
 * del Grupo 6 de plg
 */
public class Compilador extends JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indica la version de JFrame.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Fichero con el código Fuente.
	 */
	public FileReader ficheroFuente;

	/**
	 * Selector de ficheros fuente.
	 */
	private JFileChooser selFichero;

	/**
	 * Analizador sintáctico.
	 */
	private AnalizadorSintactico sintactico = new AnalizadorSintactico();

	/**
	 * Máquina virtual.
	 */
	private MaquinaVirtual maquinaV;

	/**
	 * Código objeto resultante.
	 */
	private CodigoObjeto codObj;

	/**
	 * Detector que indica si el fichero ha sido
	 * seleccionado correctamente. 
	 */
	private boolean seguir = true;

	/**
	 * Detector de presencia de error.
	 */
	private boolean error = false;

	/**
	 * Indica si queremos editar o no el contenido
	 * cargado de un fichero fuente.
	 */
	private JRadioButton jRadioEditar;

	/**
	 * Contenedor que muestra la informacion de la pestaña principal.
	 */
	private JDesktopPane jDesktopPane1;

	/**
	 * Contenedor que se usa para crear el interfaz.
	 */
	private JTabbedPane jTabbedPane1;

	/**
	 * Una aplicación de un elemento en un menú. 
	 * Un elemento de menú es esencialmente un botón
	 * colocado en una lista.
	 */
	private JMenuItem jMenuItemFuente;

	/**
	 * Una aplicación de un elemento en un menú. 
	 * Un elemento de menú es esencialmente un botón
	 * colocado en una lista.
	 */
	private JMenuItem jMenuItemGuardar;

	/**
	 * Una aplicación de un elemento en un menú. 
	 * Un elemento de menú es esencialmente un botón
	 * colocado en una lista.
	 */
	private JMenuItem jMenuItemAyuda;

	/**
	 * Una aplicación de un elemento en un menú. 
	 * Un elemento de menú es esencialmente un botón
	 * colocado en una lista.
	 */
	private JMenuItem jMenuItemInfo;

	/**
	 * Un área de visualización de la etiqueta Fuente.
	 */
	private JLabel jLabelFuente;

	/**
	 * Un área de visualización de la etiqueta Objeto.
	 */
	private JLabel jLabelObjeto;

	/**
	 * Un área de visualización de la etiqueta Memoria.
	 */
	private JLabel jLabelMemoria;

	/**
	 * Es el botón Compilar.
	 */
	private JButton jButtonCompilar;

	/**
	 * Es el botón Ejecutar.
	 */
	private JButton jButtonEjecutar;

	/**
	 * Un área de texto para el fichero fuente.
	 */
	private JTextArea jTextAreaFuente;

	/**
	 * Un panel con barras de desplazamiento
	 * para el código fuente.
	 */
	private JScrollPane jScrollPaneFuente;

	/**
	 * Un área de texto para el código objeto de la pestaña principal.
	 */
	private JTextArea jTextAreaObjeto;

	/**
	 * Un panel con barras de desplazamiento
	 * para el código objeto de la pestaña principal.
	 */
	private JScrollPane jScrollPaneObjeto;
	
	/**
	 * Un panel con barras de desplazamiento
	 * para el código objeto de la pestaña de ejecución paso a paso.
	 */
	private JScrollPane jScrollPaneObjeto2;
	
	/**
	 * Objeto que muestra el contenido de la pila.
	 */
	private JTable jTablePila;
	
	/**
	 * Un panel con barras de desplazamiento
	 * para la tabla que muestra el estadod e la memoria.
	 */
	private JScrollPane scrollMemoria;
	
	/**
	 * Objeto que muestra el contenido de la memoria.
	 */
	private JTable jTableMemoria;
	
	/**
	 * Es el botón ejecutar paso a paso.
	 */
	private JButton jButtonPaso;
	
	/**
	 * Etiqueta que nos indica la funcionalidad del panel que muestra
	 * el contenido de la pila.
	 */
	private JLabel labelPila;
	
	/**
	 * Etiqueta que nos indica la funcionalidad del panel que muestra
	 * el contenido de la memoria. 
	 */
	private JLabel memoriaLabel;
	
	/**
	 * Etiqueta que nos indica la funcionalidad del panel que muestra
	 * el código objeto en la pestaña de ejecución paso a paso.
	 */
	private JLabel labelObjeto2;
	
	/**
	 * Un área de texto para el código objeto de la pestaña de ejecución paso a paso.
	 */
	private JTextArea textAreaObjeto2;
	
	/**
	 * Contenedor que muestra la información de la pestaña de ejecución paso a paso.
	 */
	private JDesktopPane jDesktopPane2;

	/**
	 * Botón de Ejecutar Paso.
	 */
	private JButton EjecutarPaso;

	/**
	 * Un panel con barras de desplazamiento
	 * para los resultados de la ejecución
	 * del código objeto en la máquina virtual.
	 */
	private JTextArea jTextAreaMemoria;

	/**
	 * Un panel con barras de desplazamiento
	 * para el contenido de memoria y los posibles
	 * errores.
	 */
	private JScrollPane jScrollPaneMemoria;

	/**
	 * Constructora de la clase Compilador.
	 */
	public Compilador() {
		setTitle("Compilador Primera Entrega Grupo 06");
		setLocation(100, 100);
		initComponents();
	}

	/**
	 * Método privado que inicializa todos los atributos
	 * del entorno gráfico de la clase Compilador.	
	 */
	private void initComponents() {
		//Titulo de la aplicación
		setTitle("Compilador Grupo 06");
		//Etiquetas
		//Botones
		//Paneles y áreas de texto

		//Menús de la aplicación
		JMenuBar jMenuBar1 = new JMenuBar();
		JMenu jMenu1 = new JMenu();
		JMenu jMenu2 = new JMenu();
		JMenu jMenu3 = new JMenu();
		jMenuItemFuente = new JMenuItem();
		jMenuItemGuardar = new JMenuItem();
		jMenuItemAyuda = new JMenuItem();
		jMenuItemInfo = new JMenuItem();

		//Menú Fichero Fuente
		jMenu1.setText("Fichero Fuente");
		jMenuItemFuente.setText("Buscar");
		jMenuItemFuente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jMenuItemFuenteActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItemFuente);
		//Menú Guardar
		jMenuItemGuardar.setText("Guardar");
		jMenuItemGuardar.setEnabled(false);
		jMenuItemGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jMenuItemGuardarActionPerformed(evt);
			}
		});
		jMenu1.add(jMenuItemGuardar);
		//Menú Ayuda
		jMenu2.setText("Ayuda");
		jMenuItemAyuda.setText("Mostrar Ayuda");
		jMenuItemAyuda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jMenuItemAyudaActionPerformed(evt);
			}
		});
		jMenu2.add(jMenuItemAyuda);
		//Menú Componentes del Grupo
		jMenu3.setText("Componentes del Grupo");
		jMenuItemInfo.setText("Mostrar Componentes");
		jMenuItemInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jMenuItemInfoActionPerformed(evt);
			}
		});

		jMenu3.add(jMenuItemInfo);
		jMenuBar1.add(jMenu1);
		jMenuBar1.add(jMenu3);
		jMenuBar1.add(jMenu2);
		setJMenuBar(jMenuBar1);

		setBackground(new java.awt.Color(255, 255, 102));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		setSize(800, 600);
		setResizable(false);
		jTabbedPane1 = new JTabbedPane();
		getContentPane().add(jTabbedPane1);
		{
			jDesktopPane1 = new javax.swing.JDesktopPane();
			jTabbedPane1
					.addTab("Interfaz Principal", null, jDesktopPane1, null);
			{
				jLabelFuente = new JLabel();
				jDesktopPane1.add(jLabelFuente);
				jLabelFuente.setText("Código Fuente");
				jLabelFuente.setBounds(100, 30, 80, 20);
			}
			{
				jLabelObjeto = new JLabel();
				jDesktopPane1.add(jLabelObjeto);
				jLabelObjeto.setText("Código Objeto");
				jLabelObjeto.setBounds(620, 30, 80, 20);
			}
			{
				jLabelMemoria = new JLabel();
				jDesktopPane1.add(jLabelMemoria);
				jLabelMemoria.setText("Resultados");
				jLabelMemoria.setBounds(375, 370, 100, 20);
			}
			{
				jButtonCompilar = new JButton();
				jDesktopPane1.add(jButtonCompilar);
				jButtonCompilar.setText("Compilar");
				jButtonCompilar.setBounds(350, 150, 100, 20);
				jButtonCompilar.setEnabled(false);
				jButtonCompilar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButtonCompilarActionPerformed(evt);
					}
				});
			}
			{
				jButtonEjecutar = new JButton();
				jDesktopPane1.add(jButtonEjecutar);
				jButtonEjecutar.setText("Ejecutar");
				jButtonEjecutar.setBounds(350, 196, 98, 21);
				jButtonEjecutar.setEnabled(false);
				jButtonEjecutar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButtonEjecutarActionPerformed(evt);
					}
				});
			}
			{
				EjecutarPaso = new JButton();
				jDesktopPane1.add(EjecutarPaso);
				EjecutarPaso.setText("Ejecutar Paso");
				EjecutarPaso.setBounds(350, 245, 98, 21);
				EjecutarPaso.setEnabled(false);
				EjecutarPaso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jTabbedPane1.setSelectedComponent(jDesktopPane2);
						jButtonPasoActionPerformed(evt);
					}
				});
			}
			{
				jScrollPaneFuente = new JScrollPane();
				jDesktopPane1.add(jScrollPaneFuente);
				jScrollPaneFuente.setBounds(20, 60, 246, 273);
				{
					jTextAreaFuente = new JTextArea();
					jScrollPaneFuente.setViewportView(jTextAreaFuente);
					jTextAreaFuente.setColumns(20);
					jTextAreaFuente.setRows(5);
					jTextAreaFuente.setEditable(false);
				}
			}
			{
				jScrollPaneObjeto = new JScrollPane();
				jDesktopPane1.add(jScrollPaneObjeto);
				jScrollPaneObjeto.setBounds(525, 60, 246, 273);
				{
					jTextAreaObjeto = new JTextArea();
					jScrollPaneObjeto.setViewportView(jTextAreaObjeto);
					jTextAreaObjeto.setColumns(20);
					jTextAreaObjeto.setRows(5);
					jTextAreaObjeto.setEditable(false);
				}
			}
			{
				jScrollPaneMemoria = new JScrollPane();
				jDesktopPane1.add(jScrollPaneMemoria);
				jScrollPaneMemoria.setBounds(100, 400, 600, 100);
				{
					jTextAreaMemoria = new JTextArea();
					jScrollPaneMemoria.setViewportView(jTextAreaMemoria);
					jTextAreaMemoria.setColumns(10);
					jTextAreaMemoria.setRows(5);
					jTextAreaMemoria.setEditable(false);
				}
			}
			{
				jRadioEditar = new JRadioButton();
				jDesktopPane1.add(jRadioEditar);
				jRadioEditar.setBounds(100, 350, 80, 20);
				jRadioEditar.setText("Editar");
				jRadioEditar.setSelected(true);
				jRadioEditar.setBackground(new java.awt.Color(255, 255, 102));
			}
			jDesktopPane1.setBackground(new java.awt.Color(255, 255, 102));
		}
		{
			jDesktopPane2 = new JDesktopPane();
			jTabbedPane1.addTab("Interfaz Paso a Paso", null, jDesktopPane2,
					null);

			jScrollPaneObjeto2 = new JScrollPane();
			jScrollPaneObjeto2.setBounds(518, 77, 245, 273);
			jDesktopPane2.add(jScrollPaneObjeto2);

			{
				textAreaObjeto2 = new JTextArea();
				jScrollPaneObjeto2.setViewportView(textAreaObjeto2);
				textAreaObjeto2.setColumns(20);
				textAreaObjeto2.setRows(5);
				textAreaObjeto2.setEditable(false);
			}
			{
				labelObjeto2 = new JLabel();
				jDesktopPane2.add(labelObjeto2);
				labelObjeto2.setText("Código Objeto");
				labelObjeto2.setBounds(602, 42, 77, 21);
			}
			{
				memoriaLabel = new JLabel();
				jDesktopPane2.add(memoriaLabel);
				memoriaLabel.setText("Memoria de Datos");
				memoriaLabel.setBounds(329, 35, 98, 28);
			}
			{
				labelPila = new JLabel();
				jDesktopPane2.add(labelPila);
				labelPila.setText("Pila");
				labelPila.setBounds(91, 35, 28, 28);
			}
			{
				jButtonPaso = new JButton();
				jDesktopPane2.add(jButtonPaso);
				jButtonPaso.setText("Ejecutar Paso");
				jButtonPaso.setBounds(315, 378, 140, 28);
				jButtonPaso.setEnabled(false);
				jButtonPaso.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						jButtonPasoActionPerformed(evt);
					}
				});
			}
			{
				TableModel jTableMemoriaModel = new DefaultTableModel(
						new String[][] {},
						new String[] { "Dirección", "Valor" });
				jTableMemoria = new JTable();
				scrollMemoria = new JScrollPane(jTableMemoria);
				jDesktopPane2.add(scrollMemoria);
				jTableMemoria.setModel(jTableMemoriaModel);
				scrollMemoria.setBounds(245, 77, 252, 273);
			}
			{
				TableModel jTablePilaModel = new DefaultTableModel(
						new String[][] {}, new String[] { "Valor" });
				jTablePila = new JTable();
				jDesktopPane2.add(jTablePila);
				jTablePila.setModel(jTablePilaModel);
				jTablePila.setBounds(63, 77, 84, 273);
			}
		}
	}

	/**
	 * Método que se encarga de hacer el análisis tanto 
	 * léxico como sintáctico del fichero fuente seleccionado.
	 * @param evt Evento asociado a pulsar con el ratón 
	 *   bien al darle espacio sobre el botón Compilar.
	 */
	private void jButtonCompilarActionPerformed(ActionEvent evt) {
		if (ficheroFuente != null) {
			if (!jRadioEditar.isSelected()) {
				jTextAreaFuente.setEditable(false);
			} else {
				jTextAreaFuente.setEditable(true);
				jButtonEjecutar.setEnabled(false);
				jButtonPaso.setEnabled(false);
				EjecutarPaso.setEnabled(false);
			}
			File aux = null;
			try {
				PrintWriter temporal = new PrintWriter(selFichero
						.getCurrentDirectory()
						+ File.separator + "temporal.txt");
				String contenido = jTextAreaFuente.getText();
				char c = contenido.charAt(0);
				String linea = "";
				int fin = 0;
				while (fin < contenido.length() - 1) {
					if (c == '\n') {
						temporal.println(linea);
						linea = "";
					} else {
						linea = linea + c;
					}
					fin++;
					c = contenido.charAt(fin);
					if (fin + 1 == contenido.length()) {
						temporal.println(linea);
					}
				}
				temporal.close();
				aux = new File(selFichero.getCurrentDirectory()
						+ File.separator + "temporal.txt");
				FileReader fuente = new FileReader(aux);
				((AnalizadorSintactico) sintactico).analizar(fuente);
				fuente.close();
				error = ((AnalizadorSintactico) sintactico).getErrores();
				if (error != true) {
					codObj = ((AnalizadorSintactico) sintactico).getCodigoObjeto();
					jTextAreaObjeto.setText(codObj.toString(-1));
					textAreaObjeto2.setText(codObj.toString(-1));
					jButtonEjecutar.setEnabled(true);
					jButtonPaso.setEnabled(true);
					maquinaV = new MaquinaVirtual(codObj);
					EjecutarPaso.setEnabled(true);
					jTextAreaMemoria.setText("");
					jLabelMemoria.setText("Resultados");
					if (!jRadioEditar.isSelected()) {
						jButtonCompilar.setEnabled(false);
					}
				} else {
					jLabelMemoria.setText("Errores");
					String mensaje = ((AnalizadorSintactico) sintactico).getError()
							.getMensajeError();
					jTextAreaMemoria.setText(mensaje);
					jTextAreaObjeto.setText("");
					if (!jRadioEditar.isSelected()) {
						jButtonCompilar.setEnabled(false);
					}
				}
				aux.delete();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Error con el fichero");
				if (aux != null) {
					aux.delete();
				}
			}
		}
	}

	/**
	 * Método que se encarga de ejecutar el código objeto
	 * generado para el fichero fuente.
	 * @param evt Evento asociado a pulsar con el ratón 
	 * o bien al darle espacio sobre el botón Ejecutar.
	 */
	private void jButtonEjecutarActionPerformed(ActionEvent evt) {
		//Si no has inicializado variable y intentas operar con ella, salta excepción, habría que capturarla aqui.
		if (!error) {
			TPasoApaso r = maquinaV.ejecutar();
			mostrarResultados();
			actualizarEstado(r);
		}
	}

	/**
	 * Metodo que se encarga de pasar al modo ejecucion paso a paso.
	 * @param evt Evento asociado a pulsar con el ratón 
	 * o bien al darle espacio sobre el botón Ejecutar.
	 */
	private void jButtonPasoActionPerformed(ActionEvent evt) {
		if (!error) {
			TPasoApaso r = maquinaV.ejecutarPaso();
			actualizarEstado(r);
			if (r.isFin())
				mostrarResultados();
		}
	}

	/**
	 * Metodo encargado de mostrar el resultado de la ejecucion.
	 */
	private void mostrarResultados() {
		// Comprobamos errores en tiempo de ejecución.
		if (maquinaV.getError() != null) {
			jLabelMemoria.setText("Errores");
			jTextAreaMemoria.setText(maquinaV.getError().getMensajeError());
			jButtonEjecutar.setEnabled(false);
			jButtonPaso.setEnabled(false);
			EjecutarPaso.setEnabled(false);
		} else {
			// Mostramos los resultados.
			TablaSimbolos ts;
			ts = ((AnalizadorSintactico) sintactico).getTablaSimbolos();
			jTextAreaMemoria.setText(ts.toString(maquinaV.getMemoriaDatos()));
			jButtonEjecutar.setEnabled(false);
			jButtonPaso.setEnabled(false);
			EjecutarPaso.setEnabled(false);
		}
		try {
			ficheroFuente.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "El fichero ya estaba cerrado");
		}
	}

	/**
	 * Método que se encarga de mostrar la ayuda referente
	 * al uso del compilador del grupo 06 de plg.
	 * @param evt Evento asociado a hacer click izquierdo en 
	 * el menú Ayuda --> Mostrar Ayuda.
	 */
	private void jMenuItemAyudaActionPerformed(ActionEvent evt) {
		//Muestra el fichero de ayuda por pantalla
		String mensaje = "";
		try {
			BufferedReader flujo = new BufferedReader(new FileReader(
					"AyudaPLG06.txt"));
			String frase = flujo.readLine();
			while (frase != null) {
				mensaje = mensaje + frase + "\n";
				frase = flujo.readLine();
			}
			flujo.close();
			Ventana ventana = new Ventana(this, mensaje);
			ventana.setSize(502, 350);
			ventana.reSizeBoton(210, 275);
			ventana.reSizeScrollPane(2, 2, 490, 250);
			ventana.setLocation(250, 200);
			ventana
					.setTitle("Manual de ayuda para usar el compilador Grupo 06");
			ventana.setVisible(true);
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(null,
							"Coloque el fichero AyudaPLG06.txt en el directorio del proyecto");
		}
	}

	/**
	 * Método que permite elegir el fichero fuente.
	 * @param evt Evento asociado a hacer click izquierdo en 
	 * el menú Fichero Fuente --> Buscar.
	 */
	private void jMenuItemFuenteActionPerformed(java.awt.event.ActionEvent evt) {
		ficheroFuente = buscarFicheroFuente();
		if (seguir == true) {
			if (ficheroFuente == null) {
				JOptionPane.showMessageDialog(null,
						"El fichero no existe o no es válido");
			} else {
				try {
					BufferedReader flujo = new BufferedReader(ficheroFuente);
					String frase = flujo.readLine();
					String mensaje = "";
					while (frase != null) {
						mensaje = mensaje + frase + "\n";
						frase = flujo.readLine();
					}
					flujo.close();
					jTextAreaFuente.setText(mensaje);
					jButtonCompilar.setEnabled(true);
					jTextAreaMemoria.setText("");
					jLabelMemoria.setText("Resultados");
					jTextAreaObjeto.setText("");
					jMenuItemGuardar.setEnabled(true);
				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(null,
									"Se ha producido un error en la lectura del fichero");
					jButtonCompilar.setEnabled(false);
					jButtonEjecutar.setEnabled(false);
					jButtonPaso.setEnabled(false);
					EjecutarPaso.setEnabled(false);
					jMenuItemGuardar.setEnabled(false);
				}
			}
		} else {
			seguir = true;
		}
	}

	/**
	 * Método que permite guardarlas modificaciónes
	 * realizadas con la aplicación en el fichero fuente.
	 * @param evt Evento asociado a hacer click izquierdo en 
	 * el menú Fichero Fuente --> Guardar.
	 */
	private void jMenuItemGuardarActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JFileChooser guardar = new JFileChooser();
			guardar.setCurrentDirectory(new File("pruebas" + File.separator));
			FiltroPlg filtro = new FiltroPlg();
			guardar.setFileFilter(filtro);
			int opcion = guardar.showSaveDialog(null);
			if (opcion == JFileChooser.APPROVE_OPTION) {
				String ruta = guardar.getSelectedFile().getPath();
				if (!ruta.endsWith(".plg") && (!ruta.endsWith(".txt"))) {
					ruta = ruta.concat(".plg");
				}
				PrintWriter temporal = new PrintWriter(ruta);
				String contenido = jTextAreaFuente.getText();
				char c = contenido.charAt(0);
				String linea = "";
				int fin = 0;
				while (fin < contenido.length() - 1) {
					if (c == '\n') {
						temporal.println(linea);
						linea = "";
					} else {
						linea = linea + c;
					}
					fin++;
					c = contenido.charAt(fin);
					if (fin + 1 == contenido.length()) {
						temporal.println(linea);
					}
				}
				temporal.close();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Se ha producido un error guardando el fichero");
		}
	}

	/**
	 * Método que se encarga de mostrar los componentes
	 * grupo 06 de plg.
	 * @param evt Evento asociado a hacer click izquierdo en 
	 * el menú Componentes del Grupo --> Mostrar Componentes.
	 */
	private void jMenuItemInfoActionPerformed(java.awt.event.ActionEvent evt) {
		//Muestra en una ventana nueva los miembros del grupo
		String mensaje = new String();
		mensaje = "\n\n" + "  MIGUEL ANGEL CASADO HERNANDEZ\n"
				+ "  JOSE ALBERTO CONTRERAS FERNANDEZ\n"
				+ "  JAVIER DORIA DULANTO\n" + "  LUIS GONZALEZ DE PAULA\n"
				+ "  ALBERTO MAQUEDA VIÑAS\n" + "  ANTONIO MURILLO MELERO\n"
				+ "  JAVIER MURILLO YAGUE\n" + "  IRENE PEREZ MEDINA\n"
				+ "  ALMUDENA RUIZ INIESTA.";
		Ventana ventana = new Ventana(this, mensaje);
		ventana.setSize(308, 325);
		ventana.setLocation(300, 200);
		ventana.setTitle("Componentes del Grupo 06");
		ventana.setVisible(true);
	}

	/**
	 * Función que se encarga de todo el tratamiento
	 * de ficheros asociado a la elección del fichero
	 * fuente.
	 * @return Fichero fuente obtenido.
	 */
	public FileReader buscarFicheroFuente() {
		//Método que sirve para elegir el fichero fuente
		FileReader fuente;
		try {
			selFichero = new JFileChooser();
			selFichero.setApproveButtonText("Cargar");
			selFichero.setDialogTitle("Seleccione el fichero a compilar");
			selFichero
					.setCurrentDirectory(new File("pruebas" + File.separator));
			FiltroPlg filtro = new FiltroPlg();
			selFichero.setFileFilter(filtro);
			selFichero.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int opcion = selFichero.showOpenDialog(null);
			if (opcion == JFileChooser.APPROVE_OPTION) {
				fuente = new FileReader(selFichero.getSelectedFile());
			} else {
				fuente = null;
				seguir = false;
				jButtonCompilar.setEnabled(false);
				jButtonEjecutar.setEnabled(false);
				jButtonPaso.setEnabled(false);
				EjecutarPaso.setEnabled(false);
				jTextAreaFuente.setText("");
				jTextAreaFuente.setEditable(false);
				jTextAreaMemoria.setText("");
				jTextAreaObjeto.setText("");
				jMenuItemGuardar.setEnabled(false);
			}
		} catch (FileNotFoundException e) {
			fuente = null;
		}
		return fuente;
	}

	/**
	 * Metodo que se encarga de actualizar los datos al ejecutar un paso.
	 * @param r Variable que contiene la informacion sobre el estado
	 * actual de la memoria, la pila y el contador del programa.
	 */
	private void actualizarEstado(TPasoApaso r) {
		//actualizo la memoria
		ArrayList<String> p = r.getMemoriaDatos();
		String[][] aux = new String[p.size()][2];
		for (int i = 0; i < p.size(); i++) {
			aux[i][0] = Integer.toString(i);
			aux[i][1] = p.get(i);
		}

		String[] aux2 = new String[2];
		aux2[0] = "Dirección";
		aux2[1] = "Valor";
		TableModel mod = new DefaultTableModel(aux, aux2);
		jTableMemoria.setModel(mod);

		//actualizo la pila
		p = r.getPila();
		aux = new String[p.size()][1];
		for (int i = p.size(); i > 0; i--) {
			aux[i - 1][0] = p.get(p.size() - i);
		}

		aux2 = new String[1];
		aux2[0] = "Valor";
		mod = new DefaultTableModel(aux, aux2);
		jTablePila.setModel(mod);

		//actualizo los textArea de Código objeto.
		String codString = codObj.toString(r.getCp());
		jTextAreaObjeto.setText(codString);
		textAreaObjeto2.setText(codString);

		//actualizo la posición de las scrollBar de los textArea de código objeto para mostrar
		//la instruccion proxima a ejecutar.
		//TODO hay que ver por qué no funciona
		try{
		int pos = codString.indexOf("==>", 0);
		jTextAreaObjeto.setCaretPosition(pos);
		textAreaObjeto2.setCaretPosition(pos);
		}
		catch(Exception e){
		jTextAreaObjeto.setCaretPosition(0);
		textAreaObjeto2.setCaretPosition(0);
		}
		
	}

	/**
	 * Método principal que permite la ejecución del
	 * compilador.
	 * @param args Array de argumentos que se le pueden
	 * pasar al compilador aunque no se utiliza.
	 */
	public static void main(String[] args) {
		Compilador compilador = new Compilador();
		compilador.setVisible(true);
	}
}