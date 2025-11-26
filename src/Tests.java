import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * The Tests class specifies a set of tests implemented using the JUnit tool.
 * These tests use Mooshak test files as input   and generate the expected result of
 * running these tests as output. The class is implemented for testing the program.
 * To use this class you need to include the JUnit 4 library in your
 * runtime environment.
 */
public class Tests {
    @Test public void test01() { test("input1", "output1"); }
    @Test public void test02() { test("input2", "output2"); }
    @Test public void test03() { test("input3", "output3"); }
    @Test public void test04() { test("input4", "output4"); }
    @Test public void test05() { test("input5", "output5"); }
    @Test public void test06() { test("input6", "output6"); }
    @Test public void test07() { test("input7", "output7"); }
    @Test public void test08() { test("input8", "output8"); }
    @Test public void test09() { test("input9", "output9"); }
    @Test public void test10() { test("input10", "output10"); }
    @Test public void test11() { test("input11", "output11"); }
    @Test public void test12() { test("input12", "output12"); }
    @Test public void test13() { test("input13", "output13"); }
    @Test public void test14() { test("input14", "output14"); }
    @Test public void test15() { test("input15", "output15"); }
    @Test public void test16() { test("input16", "output16"); }
    @Test public void test17() { test("input17", "output17"); }
    @Test public void test18() { test("input18", "output18"); }

    private static final File BASE = new File("Tests");

    private PrintStream consoleStream;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setup() {
        consoleStream = System.out;
        System.setOut(new PrintStream(outContent));
    }

    public void test(String input, String output) {
        test(new File(BASE, input), new File(BASE, output));
    }

    public void test(File input, File output) {
        consoleStream.println("Testing!");
        consoleStream.println("Input: " + input.getAbsolutePath());
        consoleStream.println("Output: " + output.getAbsolutePath());

        String fullInput = "", fullOutput = "";
        try {
            fullInput = new String(Files.readAllBytes(input.toPath()));
            fullOutput = new String(Files.readAllBytes(output.toPath()));
            consoleStream.println("INPUT ============");
            consoleStream.println(fullInput);
            consoleStream.println("OUTPUT ESPERADO =============");
            consoleStream.println(fullOutput);
            consoleStream.println("OUTPUT =============");
        } catch(Exception e) {
            e.printStackTrace();
            fail("Erro a ler o ficheiro");
        }

        try {
            Locale.setDefault(Locale.US);
            System.setIn(new FileInputStream(input));
            Class<?> mainClass = Class.forName("Main");
            mainClass.getMethod("main", String[].class).invoke(null, new Object[] { new String[0] });
        } catch (Exception e) {
            e.printStackTrace();
            fail("Erro no programa");
        } finally {
            byte[] outPrintBytes = outContent.toByteArray();
            consoleStream.println(new String(outPrintBytes));

            assertEquals(removeCarriages(fullOutput), removeCarriages(new String(outContent.toByteArray())));
        }
    }

    private static String removeCarriages(String s) {
        return s.replaceAll("\r\n", "\n");
    }

//    /**
//     * Limpa os ficheiros .ser no diretório /data no fim de todos os testes.
//     */
//    @AfterClass
//    public static void cleanupAll() {
//        File dataDir = new File("data");
//        if (dataDir.exists() && dataDir.isDirectory()) {
//            for (File f : dataDir.listFiles()) {
//                if (f.isFile() && f.getName().endsWith(".ser")) {
//                    f.delete();
//                }
//            }
//            System.out.println("🧹 Ficheiros .ser apagados no diretório /data.");
//        }
//    }
}
