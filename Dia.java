import java.util.Arrays;

/*
Arthur Alexsander Martins Teodoro - 0022427
Wesley Henrique Batista Nunes - 0021622
*/

public class Dia
{
    private int[][] dia;

    Dia(int quantTurmas, int quantHorarios)
    {
        dia = new int[quantTurmas][quantHorarios];
    }

    public int[][] getDia()
    {
        return dia;
    }

    public void setDia(int[][] dia)
    {
        this.dia = dia;
    }

    public void setAula(int turma, int horario, int value)
    {
        this.dia[turma][horario] = value;
    }

    public int getAula(int turma, int horario)
    {
        return this.dia[turma][horario];
    }

    @Override
    public String toString()
    {
        String res = "";
        for(int i = 0; i < dia.length; i++)
        {
            res += Arrays.toString(dia[i])+"\n";
        }
        return res;
    }

    public Dia copia()
    {
        Dia newDia = new Dia(this.dia.length, this.dia[0].length);
        for(int i = 0; i < this.dia.length; i++)
        {
            for(int j = 0; j < this.dia[i].length; j++)
            {
                newDia.setAula(i, j, this.getAula(i, j));
            }
        }
        return newDia;
    }
}
