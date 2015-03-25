package client;

public class DisplayMgr {
	public gui.OpeningDialog openDialog;
	public gui.MainFrame mainFrame;
	public gui.MainHead mainHead;
	
	public DisplayMgr(){
		mainFrame = new gui.MainFrame();
		mainHead = new gui.MainHead(mainFrame);
		openDialog = new gui.OpeningDialog(mainHead);
		
		SetUp();
	}
	
	private void SetUp(){
		CurveClient.cMgr.setMainFrameAndOpeningDialog(mainFrame, openDialog);
	}
}
