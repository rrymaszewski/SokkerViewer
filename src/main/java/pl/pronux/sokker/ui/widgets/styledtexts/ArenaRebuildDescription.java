package pl.pronux.sokker.ui.widgets.styledtexts;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import pl.pronux.sokker.model.Arena;
import pl.pronux.sokker.model.ArenaProject;
import pl.pronux.sokker.model.Money;
import pl.pronux.sokker.resources.Messages;
import pl.pronux.sokker.ui.beans.ConfigBean;

public class ArenaRebuildDescription extends StyledText {

	public ArenaRebuildDescription(Composite parent, int style) {
		super(parent, style);
		this.setBackground(parent.getBackground());
		this.setFont(ConfigBean.getFontDescription());
	}
	
	public void renew() {
		this.setText(Messages.getString("arena.buildCost"));
	}
	
	public void setInfo(Arena arena, Arena newArena) {
		this.setRedraw(false);
		this.setText(String.format("%s\r\n", Messages.getString("arena.buildCost")));
		StringBuffer all = new StringBuffer(""); //$NON-NLS-1$
		ArenaProject arenaProject = arena.getCostOfRebuild(newArena);
		String stringFormat = "%-30s %15s\r\n"; //$NON-NLS-1$
		String[][] values;
		values = new String[1][2];

		if (arenaProject.getProjectCost() > 0) {
			values[0][0] = Messages.getString("arena.projectCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getProjectCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getTerracesCost() > 0) {
			values[0][0] = Messages.getString("arena.terracesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getTerracesCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getDissasemblyTerracesCost() > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyTerracesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getDissasemblyTerracesCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getFasteningCost() > 0) {
			values[0][0] = Messages.getString("arena.fasteningCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getFasteningCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getDissasemblyFasteningCost() > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyFasteningCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getDissasemblyFasteningCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getBenchesCost() > 0) {
			values[0][0] = Messages.getString("arena.benchesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getBenchesCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getDissasemblyBenchesCost() > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyBenchesCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getDissasemblyBenchesCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getSeatsCost() > 0) {
			values[0][0] = Messages.getString("arena.seatsCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getSeatsCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getDissasemblySeatsCost() > 0) {
			values[0][0] = Messages.getString("arena.dissasemblySeatsCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getDissasemblySeatsCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getRoofCost() > 0) {
			values[0][0] = Messages.getString("arena.roofCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getRoofCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		if (arenaProject.getDissasemblyRoofCost() > 0) {
			values[0][0] = Messages.getString("arena.dissasemblyRoofCost"); //$NON-NLS-1$
			values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getDissasemblyRoofCost());
			all.append(String.format(stringFormat, (Object[]) values[0]));
		}
		values[0][0] = Messages.getString("arena.all"); //$NON-NLS-1$
		values[0][1] = Money.formatDoubleCurrencySymbol(arenaProject.getAllCosts());
		all.append(String.format(stringFormat, (Object[]) values[0]));
		
		this.append(all.toString());
		this.setRedraw(true);
	}
}
