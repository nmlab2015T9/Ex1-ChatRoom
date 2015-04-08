package client;

public class DisplayMgr {
	public gui.OpeningDialog openDialog;
	public gui.MainFrame mainFrame;
	public gui.MainHead mainHead;
	public gui.OtherHead otherhead;
	
	public DisplayMgr(){
		mainFrame = new gui.MainFrame();
		otherhead = new gui.OtherHead(mainFrame);
		mainHead = new gui.MainHead(mainFrame, otherhead);
		openDialog = new gui.OpeningDialog(mainHead, otherhead);

		SetUp();
	}
	
	private void SetUp(){
		CurveClient.cMgr.setOpeningDialogAndMainhead(openDialog, mainHead);
	}
}
