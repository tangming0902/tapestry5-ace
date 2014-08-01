package org.etb.app.mixins.b;

import java.util.List;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.Grid;

/**
 * Created by daodao on 14-2-20.
 */
@MixinAfter
public class GridSortingDisabler {

    @InjectContainer
    private Grid grid;

    void setupRender() {
        if (grid.getDataSource().getAvailableRows() == 0) {
            return;
        }

        BeanModel<?> gridBeanModel = grid.getDataModel();
        List<String> propertyNames = gridBeanModel.getPropertyNames();

        for (String propertyName : propertyNames) {
            PropertyModel propertyModel = gridBeanModel.get(propertyName);
            propertyModel.sortable(false);
        }
    }
}



