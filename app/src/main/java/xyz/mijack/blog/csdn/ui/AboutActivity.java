package xyz.mijack.blog.csdn.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import xyz.mijack.blog.csdn.R;
import xyz.mijack.blog.csdn.model.Library;

public class AboutActivity extends BaseActivity {

    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        linearLayout = (LinearLayout) findViewById(R.id.open_library_list);
        List<Library> libraries = getLibraries();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < libraries.size(); i++) {
            Library library = libraries.get(i);
            View view = inflater.inflate(R.layout.list_item_open_library, linearLayout, false);
            TextView name = (TextView) view.findViewById(R.id.library_name);
            TextView content = (TextView) view.findViewById(R.id.library_content);
            String number = (i + 1) + "、";
            String diviver = "\n\t";
            SpannableString ss = new SpannableString(number + library.getName() + diviver + library.getGradle());
            ss.setSpan(new RelativeSizeSpan(0.7f), number.length() + library.getName().length() + diviver.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //0.5f表示默认字体大小的一半
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.material_design_color_grey_600)), number.length() + library.getName().length() + diviver.length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setText(ss);
            content.setText("\t\t" + library.getContent());
            linearLayout.addView(view);
        }
    }

    private List<Library> getLibraries() {
        List<Library> list = new ArrayList<>();
        try {
            InputStream inputStream = getAssets().open("open_libraries.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            Element element = document.getDocumentElement();
            NodeList libraryNodes = element.getElementsByTagName(Library.Tag);
            for (int i = 0; i < libraryNodes.getLength(); i++) {
                Element libraryElement = (Element) libraryNodes.item(i);
                Library library = new Library();
                library.setName(libraryElement.getAttribute(Library.ATTR_NAME));
                library.setContent(libraryElement.getAttribute(Library.ATTR_CONTENT));
                library.setGradle(libraryElement.getAttribute(Library.ATTR_GRADLE));
                list.add(library);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return list;
    }


}
