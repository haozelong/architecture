package cn.edu.bupt.KWICSystem.kwic.impl;

import cn.edu.bupt.KWICSystem.input.Input;
import cn.edu.bupt.KWICSystem.input.impl.InputImpl;
import cn.edu.bupt.KWICSystem.kwic.KWIC;
import cn.edu.bupt.KWICSystem.output.Impl.OutputImpl;
import cn.edu.bupt.KWICSystem.output.Output;
import cn.edu.bupt.KWICSystem.rabbitmq.Consumer;
import cn.edu.bupt.KWICSystem.rabbitmq.RabbitMq;
import cn.edu.bupt.KWICSystem.rabbitmq.Send;
import cn.edu.bupt.KWICSystem.shift.CircularShifter;
import cn.edu.bupt.KWICSystem.shift.impl.CircularShifterImpl;
import cn.edu.bupt.KWICSystem.socket.impl.SocketServerImpl;
import cn.edu.bupt.KWICSystem.sort.Alphabetizer;
import cn.edu.bupt.KWICSystem.sort.impl.AlphabetizerImpl;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class KWICImpl extends Thread implements KWIC {
    private final String CMD_PROMPT = "####################\n1、命令行输入\n2、文件输入\n3、Socket输入\n4、消息队列输入\n5、退出\n请输入序号以选择运行模式：";
    private final String INPUT_PROMPT = "请输入：";
    private final String FILE_INPUT_PROMPT = "请输入文件路径+文件名（例如C:\\Desktop\\input.txt）：";

    private boolean isCmdMode = true;

    // cmds
    private final int CMD_ADD_LINE = 100;
    private final int FILE_ADD_LINES = 101;
    private final int SOCKET_ADD_LINES = 102;
    private final int CMD_QUIT = 103;
    private final int MQ_ADD_LINES = 104;


    private int parseCmd(String cmd) {
        int cmdCode = -1;
        if("1".equals(cmd)) {
            cmdCode = CMD_ADD_LINE;
        } else if ("2".equals(cmd)) {
            cmdCode = FILE_ADD_LINES;
        }  else if ("3".equals(cmd)) {
            cmdCode = SOCKET_ADD_LINES;
        } else if ("4".equals(cmd)) {
            cmdCode = MQ_ADD_LINES;
        } else if ("5".equals(cmd)) {
            cmdCode = CMD_QUIT;
        }

        return cmdCode;
    }

    @Override
    public void execute() throws Exception {
        String line = null;
        int cmdCode = -1;

        // initialize all variables
        // input reader
        Input input = new InputImpl();

        // circular shifter
        CircularShifter shifter = new CircularShifterImpl();

        // alphabetizer
        Alphabetizer alphabetizer = new AlphabetizerImpl();

        // line printer
        Output output = new OutputImpl();

        System.out.println("欢迎来到KWIC索引系统！");

        while (!"q".equals(line)) {
            if (isCmdMode) {
                System.out.print(CMD_PROMPT);
            } else {
                if (cmdCode == CMD_ADD_LINE) {
                    System.out.print(INPUT_PROMPT);
                } else if (cmdCode == FILE_ADD_LINES) {
                    System.out.print(FILE_INPUT_PROMPT);
                }
            }
            line = input.readLine();

            if (cmdCode == MQ_ADD_LINES && !isCmdMode) {
                try {
//                            Scanner scanner = new Scanner(System.in);
//                            String x = scanner.next();
//                            System.out.println(x);
                    String x = line;
//                    Send.send(x);
                    Consumer.getMessage();
                    line = Consumer.rabbitMqRes;
                } catch(IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            if (isCmdMode) {
                cmdCode = parseCmd(line);
                switch (cmdCode) {
                    case MQ_ADD_LINES:
//                        RabbitMq.getConnection();
                        System.out.print("请输入内容：");
                        isCmdMode = false;
                        break;
                    case CMD_ADD_LINE:
                    case FILE_ADD_LINES:
                        isCmdMode = false;
                        break;
                    case SOCKET_ADD_LINES:
                        try {
                            Thread scThread = new SocketServerImpl(8080);
                            scThread.run();
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case CMD_QUIT:
                        System.out.println("Good Bye!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("请选择正确的序号！");
                        break;
                }
            } else {
                if (cmdCode == CMD_ADD_LINE) {
                    shifter.setup(line);
                } else if (cmdCode == FILE_ADD_LINES) {
                    List<String> fileLines = input.readFile(line);
                    shifter.setup(fileLines);
                } else if (cmdCode == MQ_ADD_LINES) {
                    shifter.setup(line);
                }
                if (shifter.getLineCount() == 0) {
                    System.out.println("<---内容为空！--->");
                } else {
                    // 排序移位后的结果
                    alphabetizer.alpha(shifter);

                    // 输出排序后的结果
                    if (cmdCode == CMD_ADD_LINE || cmdCode == MQ_ADD_LINES) {
                        output.print(alphabetizer);
                    } else if (cmdCode == FILE_ADD_LINES) {
                        output.write(alphabetizer);
                    }
                }
                isCmdMode = true;
            }
        }
    }

    public void run() {
        try {
            execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
