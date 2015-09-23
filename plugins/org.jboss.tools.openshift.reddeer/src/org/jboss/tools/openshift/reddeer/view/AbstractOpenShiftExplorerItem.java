package org.jboss.tools.openshift.reddeer.view;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;

public abstract class AbstractOpenShiftExplorerItem {

	protected TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	protected TreeItem item;	
	
	public AbstractOpenShiftExplorerItem(TreeItem item) {
		this.item = item;
	}
	
	protected void activateOpenShiftExplorerView() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.activate();
	}
	
	public void refresh() {
		select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();	
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	public void select() {
		activateOpenShiftExplorerView();
		item.select();
	}
	
	public void expand() {
		activateOpenShiftExplorerView();
		item.expand();
		
		// There can be some processing, wait for it
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
	}
	
	public TreeItem getTreeItem() {
		return item;
	}
}
