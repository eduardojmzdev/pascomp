package main;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import maquinaVirtual.MaquinaVirtual;
import maquinaVirtual.MaquinaVirtualImpl;
import excepciones.MVException;

import excepciones.CompiladorException;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

/**
 * @author PLG Esta clase implementa el interfaz gráfico de la aplicación.
 * 
 */
public class Ventana extends JFrame {

	private static final long serialVersionUID = 1L;
	JMenuBar menu;
	JMenu archivo, compilar, informacion;
	JMenuItem abrir, salir, normal, paso, integrantes;
	JLabel etiqueta1, etiqueta2, etiqueta3, etiqueta4;
	JButton boton1, boton2, boton3;
	JPanel panel;
	JTextArea texto1, texto2, texto3, texto4;
	File archivoAux;
	Traductor traductor;

	enum Estados {
		INI, COMPILADO, PASO
	}

	Estados estado = Estados.INI;

	/**
	 * 
	 */
	public Ventana() {

		menu = new JMenuBar();
		archivo = new JMenu("Archivo");
		abrir = new JMenuItem("Abrir");
		abrir.addActionListener(new OyenteAbrir());
		salir = new JMenuItem("Salir");
		salir.addActionListener(new OyenteSalir());
		archivo.add(abrir);
		archivo.add(new JSeparator());
		archivo.add(salir);
		menu.add(archivo);
		compilar = new JMenu("Compilar");
		normal = new JMenuItem("Normal");
		normal.addActionListener(new OyenteNormal());
		paso = new JMenuItem("Paso a Paso");
		paso.addActionListener(new OyentePaso());
		compilar.add(normal);
		compilar.add(paso);
		menu.add(compilar);
		informacion = new JMenu("Infomación");
		integrantes = new JMenuItem("Integrantes del grupo");
		integrantes.addActionListener(new OyenteIntegrantes());
		informacion.add(integrantes);
		menu.add(informacion);

		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		etiqueta1 = new JLabel("Cuadro de código");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(etiqueta1, constraints);

		etiqueta2 = new JLabel("Código objeto generado");
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(etiqueta2, constraints);

		etiqueta3 = new JLabel("Errores de compilación");
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(etiqueta3, constraints);

		etiqueta4 = new JLabel("Estado de la memoria");
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(etiqueta4, constraints);

		texto1 = new JTextArea("");
		texto1.setEditable(false);
		JScrollPane pScroll1 = new JScrollPane(texto1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
		constraints.gridheight = 2;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		panel.add(pScroll1, constraints);

		texto2 = new JTextArea("");
		texto2.setEditable(false);
		JScrollPane pScroll2 = new JScrollPane(texto2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		panel.add(pScroll2, constraints);

		texto3 = new JTextArea("");
		texto3.setEditable(false);
		JScrollPane pScroll3 = new JScrollPane(texto3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		panel.add(pScroll3, constraints);

		texto4 = new JTextArea("");
		texto4.setEditable(false);
		JScrollPane pScroll4 = new JScrollPane(texto4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		panel.add(pScroll4, constraints);

		constraints.weighty = 0.0;
		constraints.weightx = 0.0;

		boton1 = new JButton("Compilar");
		boton1.addActionListener(new OyenteNormal());
		constraints.gridx = 0;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(boton1, constraints);

		boton2 = new JButton("Paso a Paso");
		boton2.addActionListener(new OyentePaso());
		constraints.gridx = 2;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(boton2, constraints);

		boton3 = new JButton("Ejecutar");
		boton3.addActionListener(new OyenteEjecutar());
		constraints.gridx = 1;
		constraints.gridy = 9;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		panel.add(boton3, constraints);

		getContentPane().add(panel);
		setJMenuBar(menu);
	}

	// ==================== oyentes ==============================

	/**
	 * @author PLG
	 * 
	 * Esta clase es utilizada para abrir los archivos que compila nuestra
	 * aplicacion
	 * 
	 */
	class OyenteAbrir implements ActionListener {
		public void actionPerformed(ActionEvent evento) {
			texto2.setText("");
			texto3.setText("");
			texto4.setText("");
			String Text = "";
			FileReader fichero = null;
			try {
				JFileChooser abrir = new JFileChooser(System.getProperty("user.dir"));
				FileFilter filter1 = new ExtensionFileFilter("SRC", new String[] { "src" });
				abrir.setFileFilter(filter1);
				abrir.showOpenDialog(null);
				archivoAux = abrir.getSelectedFile(); // Devuelve el File que
				// vamos a leerName
				if (archivo != null) {
					texto1.setText("");
					fichero = new FileReader(archivoAux);
					BufferedReader leer = new BufferedReader(fichero);
					int cont = 1;
					while ((Text = leer.readLine()) != null) {
						texto1.append(cont + ".  " + Text + "\n"); // append
																	// Concatena
																	// la linea
						cont++; // leida
					}
					leer.close();

				}

			} catch (IOException ioe) {
				System.out.println(ioe);
			} finally {
				try {
					fichero.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @author PLG Esta clase se utiliza para mostrar en una ventana emergente
	 *         los nombres de los componentes del grupo
	 * 
	 */
	class OyenteIntegrantes implements ActionListener {
		public void actionPerformed(ActionEvent evento) {
			// JOptionPane inf = new JOptionPane();
			JOptionPane
					.showMessageDialog(
							null,
							"Carlos Fernández Agüero\n Daniel Álvarez Ramírez\n Javier Martínez Puentes\n Antonio Ariza Guerrero\n Isamu Takebe Heras\n Carla Margalef Bentabol\n Víctor Abascal Pelayo\n Jesús De Lara Gimeno\n Andrés Jiménez Sánchez\n Ignacio Fernández Cuesta\n",
							"Integrantes del grupo", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * @author PLG Esta clase se utiliza para implementar el oyente de boton
	 *         Salir
	 * 
	 */
	class OyenteSalir implements ActionListener {
		public void actionPerformed(ActionEvent evento) {
			System.exit(0);
		}
	}

	/**
	 * @author PLG Esta clase se utiliza para implementar el oyente de boton de
	 *         ejecucion Normal
	 * 
	 */
	class OyenteNormal implements ActionListener {
		public void actionPerformed(ActionEvent evento) {
			texto3.setText("");
			texto4.setText("");
			traductor = new Traductor();
			Traductor.reset();
			traductor.setEntrada(archivoAux.getPath());
			traductor.setSalida(archivoAux.getPath().substring(0, archivoAux.getPath().length() - 4));
			try {
				traductor.ejecutar();
				estado = Estados.COMPILADO;
			} catch (CompiladorException e) {
				System.out.println("Se produjeron errores al compilar:");
				System.out.println(e.getMensajeError());
				texto3.setText("Se produjeron errores al compilar:\n");
				texto3.setText(e.getMensajeError());
			} catch (Exception e) {
				System.out.println("Se produjeron errores desconocidos al compilar:");
				e.printStackTrace();
				texto3.setText("Se produjeron errores desconocidos al compilar:\n");
				texto3.setText(e.getMessage());
			}
			texto2.setText("");
			try {
				FileReader Fichero = new FileReader(traductor.getSalida() + ".mv");
				BufferedReader leer = new BufferedReader(Fichero);
				String temp;
				int cont = 1;
				while ((temp = leer.readLine()) != null) {
					texto2.append(cont + ".  " + temp + "\n"); // append
																// Concatena la
																// linea
					cont++; // leida
				}
				leer.close();
			} catch (Exception e) {
				texto3.setText("Fichero no encontrado.");
			}
			// llamada a nuestro metodo con el archivo aux

		}
	}

	/**
	 * @author PLG Esta clase se utiliza para implementar el oyente de boton de
	 *         ejecucion Paso a Paso
	 * 
	 */
	class OyentePaso implements ActionListener {

		public void actionPerformed(ActionEvent evento) {
			texto3.setText("");
			texto4.setText("");
			if (estado != Estados.INI) {
				String result = "";
				try {
					MaquinaVirtualImpl mv = (MaquinaVirtualImpl) MaquinaVirtual.obtenerInstancia();
					if (estado != Estados.PASO) {
						String[] args = new String[2];
						args[0] = traductor.getSalida() + ".mv";
						args[1] = "-b";
						result = mv.ejecutaIni(args);
						estado = Estados.PASO;
					} else {
						result = mv.ejecutaPaso();
					}
					texto4.setText(result);
				} catch (MVException e) {
					texto3.setText("[MV] Error en linea " + e.getNumLinea() + ":\n\t" + e.getError());
				} catch (Exception e) {
					texto3.setText("Se produjeron errores " + "desconocidos al compilar:\n");
					texto3.setText(e.getMessage());
				}
			} else {
				texto3.setText("No se ha compilado ningún fichero.");
			}
		}
	}

	/**
	 * @author PLG Esta clase implementa el oyente del boton Ejecutar
	 */
	class OyenteEjecutar implements ActionListener {
		public void actionPerformed(ActionEvent evento) {
			texto3.setText("");
			texto4.setText("");
			if (estado != Estados.INI) {
				String result = "";
				try {
					MaquinaVirtualImpl mv = (MaquinaVirtualImpl) MaquinaVirtual.obtenerInstancia();
					String[] args = new String[1];
					args[0] = traductor.getSalida() + ".mv";
					result = mv.ejecutaIni(args);
					estado = Estados.COMPILADO;
					texto4.setText(result);
				} catch (MVException e) {
					texto3.setText("[MV] Error en linea " + e.getNumLinea() + ":\n\t" + e.getError());
				} catch (Exception e) {
					texto3.setText("Se produjeron errores " + "desconocidos al compilar:\n");
					texto3.setText(e.getMessage());
				}
			} else {
				texto3.setText("No se ha compilado ningún fichero.");
			}
		}
	}

	/**
	 * @author PLG Esta clase limita el tipo de ficheros que se pueden abrir con
	 *         el interfaz
	 * 
	 */
	class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		public ExtensionFileFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0];
			} else {
				this.description = description;
			}
			this.extensions = (String[]) extensions.clone();
			toLower(this.extensions);
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		public String getDescription() {
			return description;
		}

		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}
}