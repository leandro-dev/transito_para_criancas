import com.leandroideias.transito.jogo_da_memoria.FragmentJogarJogoDaMemoria;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by Leandro on 19/04/2015.
 */
public class CalculateItensOnGridTest extends TestCase{

	public CalculateItensOnGridTest(){
		super();
	}


	@Test
	public void testCalculoItensNaGrid(){
		FragmentJogarJogoDaMemoria fragment = new FragmentJogarJogoDaMemoria();
		FragmentJogarJogoDaMemoria.DisposicaoItensGrid disp = fragment.calcularTamanhoItem(800, 480, 20);

		assertEquals(disp.getColunas(), 5);
		assertEquals(disp.getLinhas(), 4);

		disp = fragment.calcularTamanhoItem(900, 100, 10);
		assertEquals(disp.getColunas(), 10);
		assertEquals(disp.getLinhas(), 1);

		disp = fragment.calcularTamanhoItem(1000, 100, 10);
		assertEquals(disp.getColunas(), 10);
		assertEquals(disp.getLinhas(), 1);
	}
}
