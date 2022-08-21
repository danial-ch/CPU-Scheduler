import javax.swing.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static Lock schedulerLock = new ReentrantLock();
    static int N;
    static int M;
    static JTextArea textArea = new JTextArea();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        JFrame frame = new JFrame();

        JLabel outputLabel = new JLabel("Output:");
        outputLabel.setBounds(200,15,150,50);

        textArea.setBounds(20,60,400,370);
        textArea.setAutoscrolls(true);
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        JTextField coresField = new JTextField();
        coresField.setBounds(500,80,150,20);
        JLabel coresLabel = new JLabel("Number of cores:");
        coresLabel.setBounds(500,40,150,50);

        JTextField processField = new JTextField();
        processField.setBounds(500,190,150,20);
        JLabel processLabel = new JLabel("Number of processes:");
        processLabel.setBounds(500,150,150,50);

        JButton runButton = new JButton("Run");
        runButton.setBounds(525,280,100,35);
        runButton.addActionListener(e -> {
            N = Integer.parseInt(coresField.getText());
            M = Integer.parseInt(processField.getText());
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    startProgram();
                }
            };
            executorService.execute(r);
        });

        frame.add(runButton);
        frame.add(processLabel);
        frame.add(processField);
        frame.add(coresLabel);
        frame.add(coresField);
        frame.add(outputLabel);
        JScrollPane scroll = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBounds(20,60,400,370);
        frame.add(scroll);
        frame.setResizable(false);
        frame.setSize(700,500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setTitle("CPU Scheduler");
    }

    static void appendText(String text){
        synchronized (textArea){
            textArea.append(text + "\n");
        }
    }

    static void startProgram(){
        for (int i = 0; i < M; i++) {
            long burstTime = (new Random().nextInt(10000));
            long arrivalTime = (new Random().nextInt(1000));
            int coresNeeded = new Random().nextInt(N - 1) + 1;
            Process process = new Process(arrivalTime,burstTime,coresNeeded,i);
            Main.appendText("Created process " + i + " that requires " + coresNeeded + " cores with initial burst time : " + burstTime + " and arrival time : " + arrivalTime);
            process.run();
        }
        CPU cpu = new CPU(N);
        Scheduler.getInstance().setCpu(cpu);
        cpu.startReport();
        Scheduler.getInstance().run();
    }
}
