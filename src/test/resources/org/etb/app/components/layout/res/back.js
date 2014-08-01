Tapestry.Initializer.BackNavLayout = function(spec) {
	try {
		ace.settings.check('navbar', 'fixed');
	} catch (e) {
	}
};

Tapestry.Initializer.SideMenuLayout = function(spec) {
	try {
		ace.settings.check('main-container', 'fixed');
	} catch (e) {
	}

	try {
		ace.settings.check('sidebar', 'fixed');
	} catch (e) {
	}

	try {
		ace.settings.check('sidebar', 'collapsed');
	} catch (e) {
	}
};

Tapestry.Initializer.NavigationLayout = function(spec) {
	try {
		ace.settings.check('breadcrumbs', 'fixed');
	} catch (e) {
	}
};
