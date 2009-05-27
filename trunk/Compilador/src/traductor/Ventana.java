package compilador;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import main.Compilador;

import compilador.excepciones.CompiladorException;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
;public class Ventana extends JFrame{

	private static final long serialVersionUID = 1L;
	JMenuBar menu;
	JMenu archivo, compilar, informacion;
	JMenuItem abrir, guardar, salir, normal, paso, integrantes;
	JLabel etiqueta1, etiqueta2, etiqueta3, etiqueta4;
	JButton boton1, boton2;
	JPanel panel;
	JTextArea texto1, texto2, texto3, texto4;
	File archivoAux;
	Compilador compi;
	

	public Ventana(Compilador compi) {
		
		this.compi=compi;	
		menu = new JMenuBar();
		archivo = new JMenu("Archivo");
		abrir = new JMenuItem("Abrir");
		abrir.addActionListener(new OyenteAbrir());
		guardar = new JMenuItem("Guardar");
		//guardar.addActionListener(new OyenteGuardar());
		salir = new JMenuItem("Salir");
		salir.addActionListener(new OyenteSalir());
		archivo.add(abrir);
		archivo.add(guardar);
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
		panel.setLayout (new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		etiqueta1 = new JLabel ("Cuadro de código");
		constraints.gridx = 0; 
		constraints.gridy = 0; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		panel.add(etiqueta1, constraints);
		
		etiqueta2 = new JLabel ("Código objeto generado");
		constraints.gridx = 0; 
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		panel.add(etiqueta2, constraints);
		
		etiqueta3 = new JLabel ("Errores de compilación");
		constraints.gridx = 1; 
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		panel.add(etiqueta3, constraints);
		
		etiqueta4 = new JLabel ("Estado de la memoria");
		constraints.gridx = 2; 
		constraints.gridy = 4; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		panel.add(etiqueta4, constraints);
		
		texto1 = new JTextArea("program HolaMundo");
		texto1.setEditable(false);
		JScrollPane pScroll1 = new JScrollPane( texto1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 0; 
		constraints.gridy = 1; 
		constraints.gridwidth = 3; 
		constraints.gridheight = 2; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0; 
		constraints.weightx = 1.0; 
		panel.add (pScroll1, constraints);
		
		texto2 = new JTextArea("");
		texto2.setEditable(false);
		JScrollPane pScroll2 = new JScrollPane( texto2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 0; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0; 
		panel.add (pScroll2, constraints);
		
		texto3 = new JTextArea("");
		texto3.setEditable(false);
		JScrollPane pScroll3 = new JScrollPane( texto3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 1; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0; 
		panel.add (pScroll3, constraints);
		
		texto4 = new JTextArea("");
		texto4.setEditable(false);
		JScrollPane pScroll4 = new JScrollPane( texto4, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		constraints.gridx = 2; 
		constraints.gridy = 5; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weighty = 1.0; 
		constraints.weightx = 1.0; 
		panel.add (pScroll4, constraints);
		
		constraints.weighty = 0.0;
		constraints.weightx = 0.0; 

		boton1 = new JButton("Compilar");
		boton1.addActionListener(new OyenteNormal());
		constraints.gridx = 0; 
		constraints.gridy = 9; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		panel.add(boton1, constraints);
		
		boton2 = new JButton("Paso a Paso?");
		boton2.addActionListener(new OyentePaso());
		constraints.gridx = 2; 
		constraints.gridy = 9; 
		constraints.gridwidth = 1; 
		constraints.gridheight = 1; 
		panel.add(boton2, constraints);
		
		getContentPane().add(panel);
		setJMenuBar(menu);
	}
	
//==================== oyentes ==============================
	
	class OyenteAbrir implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			String Text="";
			try{
			   JFileChooser abrir=new JFileChooser(System.getProperty("user.dir"));
			   FileFilter filter1 = new ExtensionFileFilter("SRC", new String[] { "src"});
			   abrir.setFileFilter(filter1);
			   abrir.showOpenDialog(null);
			   archivoAux=abrir.getSelectedFile(); //Devuelve el File que vamos a leerName
			   if(archivo!=null){
			      texto1.setText("");   
			      FileReader Fichero=new FileReader(archivoAux);
			      BufferedReader leer=new BufferedReader(Fichero);
			      while((Text=leer.readLine())!=null){
			         texto1.append(Text+ "\n"); //append Concatena la linea leida
			        }
			      leer.close();
			    }                 
			  }
			  catch(IOException ioe){
			    System.out.println(ioe);
			   }
		}
	}
	
/*	class OyenteGuardar implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			try{
				System.getProperty("user.dir"); //Abre el JFileChooser 
				JFileChooser guardar=new JFileChooser(System.getProperty("user.dir"));
				FileFilter filter1 = new ExtensionFileFilter("PAS", new String[] { "pas"});
				guardar.setFileFilter(filter1);
				guardar.showSaveDialog(null); //Muestra el diálogo
				File archivo = guardar.getSelectedFile();
				String nombre= "/" + archivo.getName()+ ".pas";
				File archivoNuevo = new File(archivo.getParent()+nombre);
				
				if(archivoNuevo !=null){
					FileWriter  Guardx=new FileWriter(archivoNuevo);
					Guardx.write(texto1.getText());
					Guardx.close(); //Cierra el fichero
				}
			 }
			 catch(IOException ioe){
			 System.out.println(ioe); //Muestra por consola los errores
			} 
		}
	}
	*/
	class OyenteIntegrantes implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			//JOptionPane inf = new JOptionPane();
			JOptionPane.showMessageDialog(null,"Carlos Fernández Agüero\n Daniel Álvarez Ramírez\n Javier Martínez Puentes\n Antonio Ariza Guerrero\n Isamu Takebe Heras\n Carla Margalef Bentabol\n Víctor Abascal Pelayo\n Jesús De Lara Gimeno\n Andrés Jiménez Sánchez\n Ignacio Fernández Cuesta\n", "Integrantes del grupo", JOptionPane.INFORMATION_MESSAGE );
		}
	}
	
	class OyenteSalir implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			System.exit(0);
		}
	}
	
	class OyenteNormal implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			try{
				if(archivoAux !=null){
					FileWriter  Guardx=new FileWriter(archivoAux);
					Guardx.write(texto1.getText());
					Guardx.close(); //Cierra el fichero
				}
			 }
			 catch(IOException ioe){
				 System.out.println(ioe); //Muestra por consola los errores
			 }
			 compi.setEntrada(archivoAux.getPath());
			 compi.setSalida(archivoAux.getPath().substring(0,archivoAux.getPath().length()-4)); 
			 try{
			 compi.ejecutar();
			 }
		     catch (CompiladorException e) {
				System.out.println("Se produjeron errores al compilar:");
				System.out.println(e.getMensajeError());
			 }
		     catch (Exception e) {
				System.out.println("Se produjeron errores desconocidos al compilar:");
				e.printStackTrace();
			    } 
			 texto2.setText("");   
			 try{
				 String manolo = compi.getSalida();
				 FileReader Fichero=new FileReader(compi.getSalida()+".mv");
				 BufferedReader leer=new BufferedReader(Fichero);
				 String temp;
				 while((temp=leer.readLine())!=null){
					 texto2.append(temp+ "\n"); //append Concatena la linea leida
				 }
				 leer.close();
			 }
			 catch (Exception e){
				 System.out.println("Fichero no encontrado.");
			 }
		//llamada a nuestro metodo con el archivo aux
		
	}
	}
	
	class OyentePaso implements ActionListener{
		public void actionPerformed(ActionEvent evento){
			//95495465465
		}
	}
	
	
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