function setupMenu() {
	const {remote} = require('electron');
	const {Menu, MenuItem} = remote;

	const template = [
	  {
	    label: 'Edit',
	    submenu: [
	      {
	        label: 'Undo',
	        accelerator: 'CmdOrCtrl+Z',
	        role: 'undo'
	      },
	      {
	        label: 'Redo',
	        accelerator: 'Shift+CmdOrCtrl+Z',
	        role: 'redo'
	      },
	      {
	        type: 'separator'
	      },
	      {
	        label: 'Cut',
	        accelerator: 'CmdOrCtrl+X',
	        role: 'cut'
	      },
	      {
	        label: 'Copy',
	        accelerator: 'CmdOrCtrl+C',
	        role: 'copy'
	      },
	      {
	        label: 'Paste',
	        accelerator: 'CmdOrCtrl+V',
	        role: 'paste'
	      },
	      {
	        label: 'Select All',
	        accelerator: 'CmdOrCtrl+A',
	        role: 'selectall'
	      },
	    ]
	  },
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
	          if (focusedWindow)
	            focusedWindow.webContents.toggleDevTools();
	        }
	      },
	      {
	        label: 'Toggle Documentation',
	        accelerator: process.platform === 'darwin' ? 'Alt+Command+D' : 'Ctrl+Shift+D',
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
	        click() { require('electron').shell.openExternal('http://github.com/ThomasGaubert/openvr-notifications'); }
	      },
	      {
	        label: 'Documentation',
	        click() { require('electron').shell.openExternal('http://github.com/ThomasGaubert/openvr-notifications/wiki'); }
	      },
	      {
	        label: 'Play Store',
	        click() { require('electron').shell.openExternal('https://play.google.com/store/apps/details?id=com.texasgamer.openvrnotif'); }
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
	}

	const menu = Menu.buildFromTemplate(template);
	Menu.setApplicationMenu(menu);
}