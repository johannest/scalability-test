package org.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Viewport;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.samples.MainScreen;
import org.vaadin.samples.authentication.AccessControl;
import org.vaadin.samples.authentication.BasicAccessControl;
import org.vaadin.samples.authentication.LoginScreen;
import org.vaadin.samples.authentication.LoginScreen.LoginListener;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Main UI class of the application that shows either the login screen or the
 * main view of the application depending on whether a user is signed in.
 *
 * The @Viewport annotation configures the viewport meta tags appropriately on
 * mobile devices. Instead of device based scaling (default), using responsive
 * layouts.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("mytheme")
@Widgetset("org.vaadin.MyAppWidgetset")
public class MyUI extends UI {

    private AccessControl accessControl = new BasicAccessControl();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
    	setId("myui-ui");
    	System.out.println("init");
        Responsive.makeResponsive(this);
        setLocale(vaadinRequest.getLocale());
        getPage().setTitle("My");
        if (!accessControl.isUserSignedIn()) {
            setContent(new LoginScreen(accessControl, new LoginListener() {
                @Override
                public void loginSuccessful() {
                    showMainView();
                }
            }));
        } else {
            showMainView();
        }
    }

    protected void showMainView() {
    	System.out.println("showMainView");
        addStyleName(ValoTheme.UI_WITH_MENU);
        setContent(new MainScreen(MyUI.this));
        getNavigator().navigateTo(getNavigator().getState());
    }

    public static MyUI get() {
        return (MyUI) UI.getCurrent();
    }

    public AccessControl getAccessControl() {
        return accessControl;
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true
    		,initParams = {
            @WebInitParam(name = "disable-xsrf-protection", value = "true")
            /*,@WebInitParam(name = "syncIdCheck", value = "false")*/})
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    
    public static class MyUIServlet extends DiagnosticAndTestServlet {
    }
}
