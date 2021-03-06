package presentation;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import model.Club;
import model.Couleur;
import model.EquivalentLettre;
import model.Obs;
import controle.ControleJPopMenu;

/**
 * AfficheImage permet d'afficher la carte
 * 
 * @authors Timoth� Barbe, Florent Euvrard, Cheikh Sylla
 *
 */
/**
 * @author Timoth�
 *
 */
public class AfficheImage extends JPanel implements MouseListener,
		MouseWheelListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private Image imageCarte;
	private Obs obs;

	/**
	 * Cree et initialise un nouveau JPanel AfficheImage
	 * 
	 * @param obs
	 */
	public AfficheImage(Obs obs) {
		this.imageCarte = getToolkit().getImage(MainWindows.pathCarte);
		this.obs = obs;
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		this.addMouseMotionListener(this);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int zoom = obs.getZoom();
		int coinX = (int) obs.getCoinZoom().getX();
		int coinY = (int) obs.getCoinZoom().getY();

		// Si la largeur est plus petite que la longeur (au facteur 1.389 pres)
		if (getHeight() * 1.389 > getWidth()) {
			g.drawImage(this.imageCarte, (int) (-coinX * zoom
					* (double) getWidth() / 552), (int) (-coinY * zoom
					* (double) getWidth() / 552), zoom * getWidth(),
					(int) (zoom * getWidth() / 1.389), this);
			g.setFont(new Font("Arial", Font.BOLD, 12));

			// Dessin des points (avant les noms pour eviter la superposition)
			for (int i = 0; i < obs.getDiv().getListe().size(); i++) {
				if (obs.getTableVisible()[i]) {
					Club c = obs.getDiv().getListe().get(i);

					int x = (int) (zoom * (double) getWidth() / 552 * (c
							.getCoordonneesMatricielles()[0] - coinX));
					int y = (int) (zoom * (double) getWidth() / 552 * (c
							.getCoordonneesMatricielles()[1] - coinY));

					g.setColor(Couleur.getColor(this.obs.getReponseSolveur()[i]));
					g.fillOval(x, y, 10, 10);
				}
			}

			// Dessin des noms
			for (int i = 0; i < obs.getDiv().getListe().size(); i++) {
				if (obs.getTableVisible()[i]) {
					Club c = obs.getDiv().getListe().get(i);

					int x = (int) (zoom * (double) getWidth() / 552 * (c
							.getCoordonneesMatricielles()[0] - coinX));
					int y = (int) (zoom * (double) getWidth() / 552 * (c
							.getCoordonneesMatricielles()[1] - coinY));

					String nom = c.toString();
					g.setColor(Couleur.getColor(this.obs.getReponseSolveur()[i]));
					if (obs.getIndiceSurvole() == i) {
						g.drawString(nom, x - 5 * nom.length() / 2, y - 10);
					}
				}
			}
			// Si la largeur est plus grande que la longeur (au facteur 1.389
			// pres)
		} else {
			g.drawImage(this.imageCarte, (int) (-coinX * zoom
					* (double) getHeight() / 552 * 1.389), (int) (-coinY * zoom
					* (double) getHeight() / 552 * 1.389),
					(int) (zoom * 1.389 * getHeight()), zoom * getHeight(),
					this);
			g.setFont(new Font("Arial", Font.BOLD, 12));

			// Dessin des points (avant les noms pour eviter la superposition)
			for (int i = 0; i < obs.getDiv().getListe().size(); i++) {
				if (obs.getTableVisible()[i]) {
					Club c = obs.getDiv().getListe().get(i);

					int x = (int) (zoom * (double) getHeight() / 552 * 1.389 * (c
							.getCoordonneesMatricielles()[0] - coinX));
					int y = (int) (zoom * (double) getHeight() / 552 * 1.389 * (c
							.getCoordonneesMatricielles()[1] - coinY));

					g.setColor(Couleur.getColor(this.obs.getReponseSolveur()[i]));
					g.fillOval(x, y, 10, 10);
				}
			}

			// Dessin des noms
			for (int i = 0; i < obs.getDiv().getListe().size(); i++) {
				if (obs.getTableVisible()[i]) {
					Club c = obs.getDiv().getListe().get(i);

					int x = (int) (zoom * (double) getHeight() / 552 * 1.389 * (c
							.getCoordonneesMatricielles()[0] - coinX));
					int y = (int) (zoom * (double) getHeight() / 552 * 1.389 * (c
							.getCoordonneesMatricielles()[1] - coinY));

					String nom = c.toString();
					g.setColor(Couleur.getColor(this.obs.getReponseSolveur()[i]));
					if (obs.getIndiceSurvole() == i) {
						g.drawString(nom, x - 5 * nom.length() / 2, y - 10);
					}
				}
			}
		}

		// voyant de validation de solution
		if (obs.estRepartiHomogene())
			g.drawImage(getToolkit().getImage("Donnees/ok.png"), 20, 20, 40,
					40, this);
		else
			g.drawImage(getToolkit().getImage("Donnees/refus.png"), 20, 20, 40,
					40, this);
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			int clubclick = this.getClubPlusProche(e.getX(), e.getY());
			if (clubclick >= 0) {
				obs.setClubSelectionne(obs.getDiv().getListe().get(clubclick));
			}
		}
		if (e.getButton() == MouseEvent.BUTTON3) {
			int clubclick = this.getClubPlusProche(e.getX(), e.getY());
			if (clubclick >= 0) {
				JPopupMenu contextMenu = new JPopupMenu();
				for (int i = 0; i < obs.getDiv().getNbGroupe(); i++) {
					JMenuItem item = new JMenuItem(
							EquivalentLettre.getLettre(i));
					if (obs.getReponseSolveur()[clubclick] == i) {
						item.setIcon(new ImageIcon("Donnees/icone-tick.png"));
					}
					contextMenu.add(item);
					item.addActionListener(new ControleJPopMenu(obs, clubclick));
				}
				contextMenu.setEnabled(true);
				contextMenu.setVisible(true);
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (notches < 0) {
			int zoom = obs.getZoom() + 1;
			obs.setZoom(zoom);
			Point nouveauCoin = new Point(e.getX() * 552 / getWidth() - 400
					/ (zoom), e.getY() - 300 / zoom);
			obs.setCoinZoom(nouveauCoin);

		} else {
			obs.setZoom(1);
			obs.setCoinZoom(new Point());
		}
	}

	/**
	 * @param x
	 * @param y
	 * @return le Club le plus proche de (x,y)
	 */
	private int getClubPlusProche(int x, int y) {
		Point pointClick = this.getCoordonneesSansZoom(x, y);
		double distMini = 10;
		int rep = -1;
		for (int i = 0; i < obs.getDiv().getListe().size(); i++) {
			Club c = obs.getDiv().getListe().get(i);
			Point p = new Point((int) c.getCoordonneesMatricielles()[0],
					(int) c.getCoordonneesMatricielles()[1]);
			double dist = p.distance(pointClick);
			if (dist < distMini) {
				rep = i;
				distMini = dist;
			}
		}
		return rep;
	}

	
	/**
	 * @param x
	 * @param y
	 * @return coordonnee sur la carte a l'echelle 1
	 */
	private Point getCoordonneesSansZoom(int x, int y) {
		double facteur = 1;
		if (getHeight() * 1.389 > getWidth()) {
			facteur = (double) getWidth() / 552;
		} else {
			facteur = (double) getHeight() / 552 * 1.389;
		}
		int xf = (int) (obs.getCoinZoom().getX() + x
				/ (obs.getZoom() * facteur));
		int yf = (int) (obs.getCoinZoom().getY() + y
				/ (obs.getZoom() * facteur));
		return new Point(xf, yf);
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		int clubSurvole = this.getClubPlusProche(e.getX(), e.getY());
		obs.setIndiceSurvole(clubSurvole);
	}
}
