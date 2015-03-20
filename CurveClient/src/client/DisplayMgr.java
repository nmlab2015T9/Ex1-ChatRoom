package client;

public class DisplayMgr {
	public gui.OpeningDialog openDialog;
	public gui.MainFrame mainFrame;
	
	public DisplayMgr(){
		mainFrame = new gui.MainFrame();
		openDialog = new gui.OpeningDialog(mainFrame);
	}
}
