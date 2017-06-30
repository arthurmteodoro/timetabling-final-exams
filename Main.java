import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
Arthur Alexsander Martins Teodoro - 0022427
Wesley Henrique Batista Nunes - 0021622
*/

public class Main
{
    public static TimeTable readFile(String arq)
    {
        List<List<Integer>> curriculas = new ArrayList<>();
        int quantDisciplinas = 0;
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(arq));
            String linha = br.readLine();

            String[] dados = linha.replaceAll("\n", "").split("\t");
            TimeTable.QUANT_HORARIOS = Integer.parseInt(dados[0]);
            TimeTable.QUANT_SALA = Integer.parseInt(dados[1]);
            TimeTable.QUANT_EXAMES = Integer.parseInt(dados[2]);

            linha = br.readLine();

            while(linha != null)
            {
                dados = linha.replaceAll("\n", "").split("\t");
                quantDisciplinas = quantDisciplinas + (dados.length-1);

                List<Integer> disciplinasCurricula = new ArrayList<>();
                for(int i = 1; i < dados.length; i++)
                {
                    disciplinasCurricula.add(Integer.parseInt(dados[i]));
                }
                curriculas.add(Integer.parseInt(dados[0]), disciplinasCurricula);

                linha = br.readLine();
            }

            return new TimeTable(curriculas, quantDisciplinas);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String args[])
    {
        TimeTable time = readFile(args[0]);
        if(time != null)
        {
            //time.SAModificado(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            //caso deseje usar o metodo proposto utilizar a funcao acima
            time.SA(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));

            System.out.println("Objetivo: "+time.funcaoObjetivo(time.getDias(), time.getDisciplinas()));
            for(int i = 0; i < 5; i++)
            {
                System.out.println("Dia "+(i+1));
                System.out.println(time.getDias()[i]);
            }
            System.out.println("Vetor Quant. Restante: "+ Arrays.toString(time.getDisciplinas()));
        }
    }
}
