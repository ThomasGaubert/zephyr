function setupMenu() {
	const {remote} = require('electron');
	const {Menu, MenuItem} = remote;

	const template = [
	  {
	    label: 'View',
	    submenu: [
	      {
	        label: 'Reload',
	        accelerator: 'CmdOrCtrl+R',
	        click(item, focusedWindow) {
	          if (focusedWindow) focusedWindow.reload();
	        }
	      },
	      {
	        label: 'Toggle Full Screen',
	        accelerator: process.platform === 'darwin' ? 'Ctrl+Command+F' : 'F11',
	        click(item, focusedWindow) {
	          if (focusedWindow)
	            focusedWindow.setFullScreen(!focusedWindow.isFullScreen());
	        }
	      },
	      {
	        label: 'Toggle Developer Tools',
	        accelerator: process.platform === 'darwin' ? 'Alt+Command+I' : 'Ctrl+Shift+I',
	        click(item, focusedWindow) {
	            $(".dev-tools").toggle();
	        }
	      },
	    ]
	  },
	  {
	    label: 'Window',
	    role: 'window',
	    submenu: [
	      {
	        label: 'Minimize',
	        accelerator: 'CmdOrCtrl+M',
	        role: 'minimize'
	      },
	      {
	        label: 'Close',
	        accelerator: 'CmdOrCtrl+W',
	        role: 'close'
	      },
	    ]
	  },
	  {
	    label: 'Links',
	    role: 'help',
	    submenu: [
	      {
	        label: 'GitHub',
	        click() { require('electron').shell.openExternal('http://github.com/ThomasGaubert/zephyr'); }
	      },
	      {
	        label: 'Documentation',
	        click() { require('electron').shell.openExternal('http://github.com/ThomasGaubert/zephyr/wiki'); }
	      },
	      {
	        label: 'Play Store',
	        click() { require('electron').shell.openExternal('https://play.google.com/store/apps/details?id=com.texasgamer.zephyr'); }
	      },
	    ]
	  },
	];

	if (process.platform === 'darwin') {
	  const name = require('electron').remote.app.getName();
	  template.unshift({
	    label: name,
	    submenu: [
	      {
	        label: 'About ' + name,
	        role: 'about'
	      },
	      {
	        type: 'separator'
	      },
	      {
	        label: 'Services',
	        role: 'services',
	        submenu: []
	      },
	      {
	        type: 'separator'
	      },
	      {
	        label: 'Hide ' + name,
	        accelerator: 'Command+H',
	        role: 'hide'
	      },
	      {
	        label: 'Hide Others',
	        accelerator: 'Command+Alt+H',
	        role: 'hideothers'
	      },
	      {
	        label: 'Show All',
	        role: 'unhide'
	      },
	      {
	        type: 'separator'
	      },
	      {
	        label: 'Quit',
	        accelerator: 'Command+Q',
	        click() { remote.app.quit(); }
	      },
	    ]
	  });
	  // Window menu.
	  template[3].submenu.push(
	    {
	      type: 'separator'
	    },
	    {
	      label: 'Bring All to Front',
	      role: 'front'
	    }
	  );
	} else if (process.platform === 'win32') {
		const name = require('electron').remote.app.getName();
	  template.unshift({
	    label: 'File',
	    submenu: [
	      {
	        label: 'Check for Updates...',
	        click() { remote.autoUpdater.checkForUpdates() }
	      },
	      {
	        label: 'Quit',
	        accelerator: 'Alt+F4',
	        click() { remote.app.quit(); }
	      },
	    ]
	  });
	}

	const menu = Menu.buildFromTemplate(template);
	Menu.setApplicationMenu(menu);
}