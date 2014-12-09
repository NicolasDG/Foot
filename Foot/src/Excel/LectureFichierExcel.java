package Excel;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class LectureFichierExcel {

	private static Workbook workbook;

	public static int getNumeroLastLigne(int sheet, int colonne) {
		return workbook.getSheet(sheet).getColumn(colonne).length;
	}

	public static int getNumeroLastColonne(int sheet, int ligne) {
		return workbook.getSheet(sheet).getRow(ligne).length;
	}

	public static String[][] getAllTab(int sheet) {
		String[][] tab = new String[getNumeroLastLigne(sheet, 0)][getNumeroLastColonne(sheet, 0)];
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[0].length; j++) {
				tab[i][j] = workbook.getSheet(sheet).getCell(i, j).getContents();
			}
		}
		return tab;

	}

	public static String toString(String[][] tab) {
		String s = "";
		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[0].length; j++) {
				s += tab[i][j] + " ";
			}
			s += "\n";
		}
		return s;
	}

	public static void main(String[] args) {
		workbook = null;
		try {
			/* R�cup�ration du classeur Excel (en lecture) */
			workbook = Workbook.getWorkbook(new File("Donnees/test.xls"));

			/*
			 * Un fichier excel est compos� de plusieurs feuilles, on y acc�de
			 * de la mani�re suivante
			 */
			Sheet sheet = workbook.getSheet(0);

			/*
			 * On acc�de aux cellules avec la m�thode getCell(indiceColonne,
			 * indiceLigne)
			 */
			Cell a1 = sheet.getCell(0, 0);

			/* On peut �galement le faire avec getCell(nomCellule) */
			Cell c3 = sheet.getCell("C3");

			/*
			 * On peut r�cup�rer le contenu d'une cellule en utilisant la
			 * m�thode getContents()
			 */
			String contenuA1 = a1.getContents();
			String contenuC3 = c3.getContents();

			System.out.println(contenuA1);
			System.out.println(contenuC3);

			System.out.println(toString(getAllTab(0)));

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				/* On ferme le worbook pour lib�rer la m�moire */
				workbook.close();
			}
		}
	}

}
