import org.junit.Assert;
import org.junit.Test;
import org.uengine.kernel.view.ActivityView;
import org.uengine.modeling.ElementView;
import org.uengine.modeling.layout.FlowLayout;
import org.uengine.modeling.layout.GridLayout;
import org.uengine.modeling.layout.Layout;

public class LayoutTest{

    @Test
    public void gridLayout(){

        GridLayout layout = new GridLayout();
        int spacing = 50;
        layout.setSpacing(spacing);

        ElementView elementView1 = new ActivityView();
        elementView1.setWidth(100);
        elementView1.setHeight(100);

        ElementView elementView2 = new ActivityView();
        elementView2.setWidth(100);
        elementView2.setHeight(100);

        layout.add(elementView1);
        layout.add(elementView2);

        layout.layout();

        System.out.println(elementView1.getY());
        System.out.println(elementView2.getY());

        //Assert.assertEquals(elementView2.getY() - elementView1.getY(), spacing + elementView2.getHeight());


    }

    @Test
    public void flowLayout(){

        FlowLayout layout = new FlowLayout();
        int spacing = 50;

        ElementView elementView1 = new ActivityView();
        elementView1.setWidth(100);
        elementView1.setHeight(100);

        ElementView elementView2 = new ActivityView();
        elementView2.setWidth(100);
        elementView2.setHeight(100);

        layout.add(elementView1);
        layout.add(elementView2);

        layout.layout();

        System.out.println(elementView1.getX());
        System.out.println(elementView2.getX());

//        Assert.assertEquals(elementView2.getY() - elementView1.getY(), spacing + elementView2.getHeight());


    }



}