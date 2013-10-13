/*
 * class Servidor
 * date 07/08/2013
 * copyright Cherubini
 */
package br.com.bluetooth;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 * @author Leonardo Cherubini
 * @version 1.0
 */
public class Servidor extends Application {
    
    /* Container principal */
    private Scene scene;
    
    /* Tipo de layout grid */
    private GridPane root;

    /* Label auxiliar da barra de progesso */
    private Label label;
    
    /* Box responsavel pela barra de progresso */
    private HBox hb; 
    
    /* Barra de progresso */
    private ProgressBar progressBar;
    
    /* Barra de progresso circular */
    private ProgressIndicator progressIndicator;
    
    /* Box responsavel pela textArea */
    private HBox hb2;
    
    /* Area de texto esquerda */
    private TextArea textArea;
    
    /* Box responsavel pela textArea2 */
    private HBox hb3;
    
    /* Area de texto direita */
    private TextArea textArea2;
    
    /* Label auxiliar do serverButton */
    private Label label2;
    
    /* Box responsavel pelo serverButton */
    private HBox hb4;
    
    /* Botao responsavel por ligar o servidor*/
    private Button serverButton;
    
    /* Variavel responsavel pela conexao bluetooth */
    private StreamConnection connection;
    
    private String text;
    
    @Override
    public void start(Stage stage) {
        /* Atribui um valor a tela*/
        stage.setTitle("Transferência de arquivo Bluetooth");
        
        /* Inicializa os componantes */
        initComponents();
        
        /* Inicializa os eventos dos componentes */
        initListeners();
        
        /* Adicionando scene ao Stage */
        stage.setScene(scene);
        
        /* Ativando a exibicao da aplicacao */
        stage.show();
    }
    
    /**
     * Metodo responsavel pela inicializacao dos componentes
     * @return void
     */
    private void initComponents() {
        root = new GridPane(); // inicializando o laytou grid
        root.setPadding(new Insets(5)); // aumentando o padding nos quadros lados do layout
        root.setHgap(5); // configurando o espacamento horizontal dos elementos
        root.setVgap(5); // configurando o espacamento vertical dos elementos
        
        scene = new Scene(root, 350, 150, Color.WHITE); // configurando o scene com o tipo de layout e a dimensao
        
        label = new Label(); // inicializando o label
        label.setText("Transferindo Arquivo:"); // adicionando um texto para o label
        root.add(label, 0, 0); // adicionando o label ao layout
        
        progressBar = new ProgressBar(); // inicializando a barra de progresso
        progressBar.setProgress(0); // cirando um valor inicial para a barra de progresso
        
        progressIndicator = new ProgressIndicator(); // inicializando a barra de progresso circular
        progressIndicator.setProgress(0); // cirando um valor inicial para a barra de progresso circular
        
        hb = new HBox(); // inicializando o box da barra de progresso
        hb.setAlignment(Pos.CENTER); // posicionando o box de maneira central
        hb.getChildren().addAll(progressBar, progressIndicator); // adicionando a barra de progresso no hb
        root.add(hb, 1, 0); // adicionando o hb no container

        textArea = new TextArea(); // inicializando a area de texto esquerda
        textArea.setStyle("-fx-font-size:10px;"); // estilizando o tamanho da fonte do texto para 10px
        textArea.setEditable(false); // desabilitando a edicao do texto
        textArea.setPrefSize(130, 70); // configurando a dimensao da area de texto
        
        hb2 = new HBox(); // inicializando o box da area de texto esquerda
        hb2.setAlignment(Pos.CENTER); // posicionando o box de maneira central
        hb2.getChildren().addAll(textArea); // adicionando a area de texto no hb2
        root.add(hb2, 0, 1); // adicionando o hb2 no container
        
        textArea2 = new TextArea(); // inicializando a area de texto direita
        textArea2.setStyle("-fx-font-size:10px;"); // estilizando o tamanho da fonte do texto para 10px
        textArea2.setEditable(false); // desabilitando a edicao do texto
        textArea2.setPrefSize(130, 70); // configurando a dimensao da area de texto 
        
        hb3 = new HBox(); // inicializando o box da area de texto direita
        hb3.setAlignment(Pos.CENTER); // posicionando o box de maneira central
        hb3.getChildren().addAll(textArea2); // adicionando a area de texto no hb3
        root.add(hb3, 1, 1); // adicionando o hb3 no container
        
        serverButton = new Button(); // inicializando o botao
        serverButton.setText("Ligar Servidor"); // configurando o texto do botao  
        
        hb4 = new HBox(); // inicializando o box do botao
        hb4.setAlignment(Pos.CENTER); // posicionando o box de maneira central
        hb4.getChildren().addAll(serverButton); // adicionando o botao no hb3
        root.add(hb4, 1, 2); // adicionando o hb4 no container

        label2 = new Label(); // inicializando o label2 
        label2.setText("Não Conectado"); // adicionando um texto para o label2
        root.add(label2, 0, 2); // adicionando o label2 ao layout
        
           
    }
    
    /**
    * Metodo responsavel pela inicializacao dos eventos dos componentes
    * @return void
    */
    private void initListeners() {
        // criando um evento para o botao serverButton
        serverButton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                // abrindo uma conexao com o cliente e retornando seu nome
                String conexao = conectar();
                if(!conexao.equals("false")) {
                    // alterando o estado do label com o nome do cliente
                    label2.setText("Conectado: " + conexao);
                    // alterando a cor do label para verde
                    label2.setStyle("-fx-text-fill: green;");
                }
                new Dados(connection).start();
                
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Metodo responsavel por fazer a conexao do servidor com o dispositivo externo
     * @return String
     * @exception IOException
     */
    private String conectar() {
        String conexao = "";
        
        // criando uma chave UUID para a conexao bluetooth
	UUID uuid = new UUID("0000110100001000800000805F9B34FB", false);
                
	// criando um servico com a url do servidor
	String connectionString = "btspp://localhost:" + uuid
				+ ";name=Sample SPP Server";
	try {
            // abrindo o servidor para a conexao com o cliente
            StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector
					.open(connectionString);
                        
            // abrindo uma conexao com o cliente
            connection = streamConnNotifier.acceptAndOpen();

            // dispositivo conectado ao servidor
            RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);

            // nome do dispositivo conectado ao servidor
            conexao = dev.getFriendlyName(true);
                        
	} catch (IOException e) {
            e.printStackTrace();
                        
            // atribuindo false para se caso ocorrer erro de conexao
            conexao = "false";
	}
            // retornando conexao
            return conexao;
    }
    
    /**
     * Classe responsavel pela leitura de dados
     * @author Leonardo Cherubini
     * @version 1.0
     */
    private class Dados extends Thread {
        
        /* Variavel responsavel pela conexao bluetooth */
        private StreamConnection connection;
        
        /* Variavel responsavel por armazenar o valor total do arquivo em um inteiro */
        private int total = 0; 
        
        /* Variavel responsavel por armazenar o tamanho total do arquivo recebido */
        private float tamanho = 0;
        
        /* Variavel responsavel por armazenar os dados do arquivo recebido */
        private String textoFinal = "";
        
        public Dados(StreamConnection connection) {
            this.connection = connection;
        }
        
        public void run() {
            try {
		
                // fuxo para entrada de dados
		DataInputStream input = connection.openDataInputStream();

                // variavel responsavel por receber o nome do arquivo
		String arquivoNome = input.readUTF(); 
			
                // tamanho total do arquivo em bytes
		long bytes = input.readLong();      
                     
                // classe responsavel por formatacao de valores decimais
                DecimalFormat format = new DecimalFormat();  
                format.setMaximumFractionDigits(2);  
                format.setMinimumFractionDigits(1);
                
                // tamanho total do arquivo recebido
                float tamanhoTotal = bytes + 8f;
                
                // formatando o tamanho para mb
                tamanho = (tamanhoTotal + 8f)/ 1_000_000;
                
                // armazena o tamanho total do arquivo em mb
                textoFinal = "Tamanho: " + format.format(tamanho) + " mb\n";
                
                // adicionando o valor no textArea
                textArea2.setText(textoFinal);
                
                // limpando os valores em textArea
                textArea.setText("");

                // fluxo de saido do arquivo recebido
                FileOutputStream arquivo = new FileOutputStream(arquivoNome);
                
                // vetor responsavel por receber todo o tamanho do arquivo
		byte[] buf = new byte[4096];
                
                // tempo inicial da contagem da transmissao do arquivo
                long tempoInicial = System.currentTimeMillis();
                
                // tempo do calcula da velocidade em tempo de execucao
                long tempoReal = 0;
                
                // tempo final da transmissao
                long tempoFinal = 0;
                
                // tempo da velocidade de transmissao
                long tempoTotal = 0;
                
                // converte o tempo da velocidade em kbps
                long dado = 0;
                
                // armazena os dados recebidos para a utilizacao da barra de progresso
                double con = 0;
                
		while (true) {
                    
                    // recebe uma parte do arquivo
                    int len = input.read(buf);
                       
                    // se o valor for negativo cancela a transmissao
                    if(len == -1) break;
                    
                    // armazena todo valor do arquivo em um inteiro
                    total = total + len;
                    
                    // armazena todos dados do arquivo de forma legivel para o progressBar
                    con = con + (len / tamanhoTotal);
                    
                    try {
                        // atualiza o valor da barra de progresso
                        progressIndicator.setProgress(con);
                        
                        // atualiza o valor da barra de progresso circular
                        progressBar.setProgress(con);
                    } catch (StringIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    // cria o arquivo com os dados recebidos
                    arquivo.write(buf, 0, len);
                    
                    // recebe o tempo atual de execucao
                    tempoReal = System.currentTimeMillis();
                    
                    // calcula o tempo atual para definir a velocidade
                    tempoTotal = (tempoReal - tempoInicial)/1000;
                    
                    // converte o tamanha atual da arquvivo recebido em kb
                    dado = total/1000;
                    
                    if(tempoTotal > 0) {
                        
                        // adiciona o valor da velocidade de transmissao no textArea
                        textArea.setText("Velocidade: "+dado/tempoTotal+" kbps");
                    }
                }
                
                // captura o tempo final da transmissao
                tempoFinal = System.currentTimeMillis();
                
                // calcula o tempo total de transmissao
                float tempo = (tempoFinal - tempoInicial)/1000;
                
                // calcula a velocidade media
                float media = (bytes / tempo) / 1000;
                
                // armazena todos os dados de velocidade
                textoFinal = textoFinal + "Tempo Total: "+tempo+" s\n"
                            + "V. Média: " + format.format(media) + " kbps";
                
                // adiciona os dados no textArea2
                textArea2.setText(textoFinal);

                // limpa o fluxo      
		arquivo.flush();
                
                // fecha o fluxo
		arquivo.close(); 
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
