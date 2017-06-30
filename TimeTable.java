import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
Arthur Alexsander Martins Teodoro - 0022427
Wesley Henrique Batista Nunes - 0021622
*/

public class TimeTable
{
    private Dia[] dias;
    private int[] disciplinas;
    private List<List<Integer>> curriculas;
    private int quantDisciplinas;
    public static int QUANT_HORARIOS = 0;
    public static int QUANT_SALA = 0;
    public static int QUANT_EXAMES = 0;
    private static int QUANT_VIZINHO = 10;

    TimeTable(List<List<Integer>> curriculas, int quantDisciplinas)
    {
        this.curriculas = curriculas;
        this.quantDisciplinas = quantDisciplinas;
        this.disciplinas = new int[quantDisciplinas];

        for(int i = 0; i < quantDisciplinas; i++)//seta que falta um exame para todas as disciplinas
        {
            disciplinas[i] = QUANT_EXAMES;
        }

        this.dias = new Dia[5];
        for(int i = 0; i < 5; i++)
        {
            dias[i] = new Dia(quantDisciplinas, QUANT_HORARIOS);
        }
    }

    public Dia[] getDias()
    {
        return dias;
    }

    public void setDias(Dia[] dias)
    {
        this.dias = dias;
    }

    public int[] getDisciplinas()
    {
        return disciplinas;
    }

    public void setDisciplinas(int[] disciplinas)
    {
        this.disciplinas = disciplinas;
    }

    public List<List<Integer>> getCurriculas()
    {
        return curriculas;
    }

    public void setCurriculas(List<List<Integer>> curriculas)
    {
        this.curriculas = curriculas;
    }

    public void geraSolucaoInicial()
    {
        Random rand = new Random();
        int randQuantSetar = rand.nextInt(quantDisciplinas);
        for(int i = 0; i < randQuantSetar; i++)
        {
            int dia = rand.nextInt(5);

            int horario = rand.nextInt(QUANT_HORARIOS);
            while(dias[dia].getAula(i, horario) == 1)
            {
                horario = rand.nextInt(QUANT_HORARIOS);
            }

            dias[dia].setAula(i, horario, 1);
            disciplinas[i]--;
        }
    }

    public int funcaoObjetivo(Dia[] horarios, int[] displinas)
    {
        int objetivo = 0;
        for(int d : displinas)//verifica se todas os exames sao aplicados
        {
            if(d != 0)
                if(d > 0)
                    objetivo += d;
                else
                    objetivo += (d*(-1))*100;
        }

        for(int i = 0; i < horarios.length; i++)//verifica se existe a quantidade de salas
        {
            for(int j = 0; j < QUANT_HORARIOS; j++)
            {
                int contSalas = 0;
                for(int k = 0; k < quantDisciplinas; k++)
                {
                    if(horarios[i].getAula(k,j) == 1)
                        contSalas++;
                }

                if(contSalas > QUANT_SALA)
                    objetivo += objetivo+300;
            }
        }

        for(int i = 0; i < horarios.length; i++)
        {
            for(int j = 0; j < quantDisciplinas; j++)
            {
                int curricula = 0;
                for(List<Integer> c : this.curriculas)
                {
                    if(c.contains(j))
                        break;
                    curricula++;
                }

                for(int k = 0; k < QUANT_HORARIOS; k++)
                {
                    for(Integer displinasCurricula : this.curriculas.get(curricula))
                    {
                        if(displinasCurricula != j)
                        {
                            if(dias[i].getAula(displinasCurricula, k) == 1 && dias[i].getAula(j, k) == 1)
                                objetivo++;
                        }
                    }
                }
            }
        }

        return objetivo;
    }

    public Dia[] copiaDias(Dia[] dias)
    {
        Dia[] newDias = new Dia[dias.length];
        for(int i = 0; i < newDias.length; i++)
        {
            newDias[i] = dias[i].copia();
        }
        return newDias;
    }

    public Object[] gerarVizinhos(Dia[] dias, int[] disciplinas, int quantVizinhos)
    {
        //int melhorObjetivo = this.funcaoObjetivo(dias, disciplinas);
        int melhorObjetivo = Integer.MAX_VALUE;
        Dia[] melhorSolucao = copiaDias(dias);
        int[] melhorDisciplinas = Arrays.copyOf(disciplinas, disciplinas.length);

        Random rand = new Random();
        for(int i = 0; i < quantVizinhos; i++)
        {
            Dia[] diasVizinho = copiaDias(dias);
            int[] disciplinasVizinho = Arrays.copyOf(disciplinas, disciplinas.length);

            for(int j = 0; j < 2; j++)
            {
                int dia = rand.nextInt(5);
                int pertHorario = rand.nextInt(QUANT_HORARIOS);
                int pertTurma = rand.nextInt(quantDisciplinas);

                diasVizinho[dia].setAula(pertTurma, pertHorario, (diasVizinho[dia].getAula(pertTurma, pertHorario) ^ 1));
                if(diasVizinho[dia].getAula(pertTurma, pertHorario) == 1)
                    disciplinasVizinho[pertTurma]--;
                else
                    disciplinasVizinho[pertTurma]++;
            }

            int objetivoVizinho = this.funcaoObjetivo(diasVizinho, disciplinasVizinho);
            if(objetivoVizinho < melhorObjetivo && objetivoVizinho >= 0)
            {
                melhorSolucao = copiaDias(diasVizinho);
                melhorDisciplinas = Arrays.copyOf(disciplinasVizinho, disciplinasVizinho.length);
                melhorObjetivo = objetivoVizinho;
            }

        }

        Object[] retorno = new Object[2];
        retorno[0] = melhorSolucao;
        retorno[1] = melhorDisciplinas;
        return retorno;
    }

    public double esquemaResfriamento(double temp)
    {
        return temp*0.95;
    }

    public boolean criterioAceitacao(int melhorObjetivo, int objetivo, double temp)
    {
        double criterio = Math.exp((melhorObjetivo - objetivo)/temp);
        double sorteado = Math.random();

        return (sorteado < criterio);
    }

    public void SA(int tempInicial, int tempFinal, int quantReaquecimento)
    {
        this.geraSolucaoInicial();

        Dia[] overAllDias = this.copiaDias(this.getDias());
        int[] overAllDisciplina = Arrays.copyOf(this.disciplinas, this.disciplinas.length);
        int overAllObjetivo = this.funcaoObjetivo(overAllDias, overAllDisciplina);

        Dia[] melhorDias = this.copiaDias(this.getDias());
        int[] melhorDisciplina = Arrays.copyOf(this.disciplinas, this.disciplinas.length);
        int melhorObjetivo = this.funcaoObjetivo(overAllDias, overAllDisciplina);

        for(int i = 0; i < quantReaquecimento; i++)
        {
            double temp = tempInicial;

            while(temp > tempFinal)
            {
                temp = esquemaResfriamento(temp);

                Object[] melhorVizinho = this.gerarVizinhos(melhorDias, melhorDisciplina, QUANT_VIZINHO);
                int objetivoVizinho = this.funcaoObjetivo((Dia[]) melhorVizinho[0], (int[]) melhorVizinho[1]);
                if(objetivoVizinho < melhorObjetivo)
                {
                    melhorObjetivo = objetivoVizinho;
                    melhorDias = this.copiaDias((Dia[]) melhorVizinho[0]);
                    melhorDisciplina = Arrays.copyOf((int[]) melhorVizinho[1], ((int[]) melhorVizinho[1]).length);
                }else if(criterioAceitacao(melhorObjetivo, objetivoVizinho, temp))
                {
                    melhorObjetivo = objetivoVizinho;
                    melhorDias = this.copiaDias((Dia[]) melhorVizinho[0]);
                    melhorDisciplina = Arrays.copyOf((int[]) melhorVizinho[1], ((int[]) melhorVizinho[1]).length);
                }

                if(melhorObjetivo < overAllObjetivo)
                {
                    overAllDias = this.copiaDias(melhorDias);
                    overAllDisciplina = Arrays.copyOf(melhorDisciplina, melhorDisciplina.length);
                }
            }

        }

        this.dias = overAllDias;
        this.disciplinas = overAllDisciplina;
    }

    public void SAModificado(int tempInicial, int tempFinal, int quantReaquecimento)
    {
        this.geraSolucaoInicial();

        Dia[] overAllDias = this.copiaDias(this.getDias());
        int[] overAllDisciplina = Arrays.copyOf(this.disciplinas, this.disciplinas.length);
        int overAllObjetivo = this.funcaoObjetivo(overAllDias, overAllDisciplina);

        Dia[] melhorDias = this.copiaDias(this.getDias());
        int[] melhorDisciplina = Arrays.copyOf(this.disciplinas, this.disciplinas.length);
        int melhorObjetivo = this.funcaoObjetivo(overAllDias, overAllDisciplina);

        for(int i = 0; i < quantReaquecimento; i++)
        {
            double temp = tempInicial;

            while(temp > tempFinal)
            {
                Object[] melhorVizinho = this.gerarVizinhos(melhorDias, melhorDisciplina, QUANT_VIZINHO);
                int objetivoVizinho = this.funcaoObjetivo((Dia[]) melhorVizinho[0], (int[]) melhorVizinho[1]);
                if(objetivoVizinho < melhorObjetivo)
                {
                    melhorObjetivo = objetivoVizinho;
                    melhorDias = this.copiaDias((Dia[]) melhorVizinho[0]);
                    melhorDisciplina = Arrays.copyOf((int[]) melhorVizinho[1], ((int[]) melhorVizinho[1]).length);
                }else if(criterioAceitacao(melhorObjetivo, objetivoVizinho, temp))
                {
                    melhorObjetivo = objetivoVizinho;
                    melhorDias = this.copiaDias((Dia[]) melhorVizinho[0]);
                    melhorDisciplina = Arrays.copyOf((int[]) melhorVizinho[1], ((int[]) melhorVizinho[1]).length);
                }
                else
                {
                    temp = esquemaResfriamento(temp);
                }

                if(melhorObjetivo < overAllObjetivo)
                {
                    overAllDias = this.copiaDias(melhorDias);
                    overAllDisciplina = Arrays.copyOf(melhorDisciplina, melhorDisciplina.length);
                }
            }

        }

        this.dias = overAllDias;
        this.disciplinas = overAllDisciplina;
    }
}
