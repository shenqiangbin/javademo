package JsoupDemo;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <dependency>
 * <groupId>org.jsoup</groupId>
 * <artifactId>jsoup</artifactId>
 * <version>1.16.1</version>
 * </dependency>
 */
public class Test {
    public static void main(String[] args) {

        String txt = "<InventionTitle id=\"title1\">灵活多样的人脸图像老化生成系统 InventionTitle </InventionTitle><TechnicalField id=\"technical-field001\"><p id=\"p0001\">技术领域</p><p id=\"p0002\" number=\"0001\">本发明涉及图像处理技术领域，具体的，涉及灵活多样的人脸图像老化生成系统。</p></TechnicalField><BackgroundArt id=\"background-art001\"><p id=\"p0003\">背景技术</p><p id=\"p0004\" number=\"0002\">人脸老化旨在保持人脸身份信息的同时，模拟不同年龄段的面部外观变化，在年龄估计、跨年龄人脸识别、影视创作以及医美等方面有实际的落地应用前景。在过去的几十年里，深度学习的快速发展推动了人脸老化的研究工作。目前人脸老化主要面临三个问题：首先，之前基于GAN的方法往往很难鲁棒地生成高质量的老化结果，在实际生成过程中很多结果存在明显伪影；其次，之前的老化方法往往以固定的年龄标签作为输入，大大限制了人脸老化的灵活程度；最后，之前的老化方法忽略老化的多样性，因为受到环境等复杂因素的影响，只生成一种老化模式是很不科学的。总结来说，上述三个问题都是老化中亟待解决的问题。</p></BackgroundArt><Disclosure id=\"disclosure001\"><p id=\"p0005\">发明内容</p><p id=\"p0006\" number=\"0003\">本发明提出灵活多样的人脸图像老化生成系统，解决了相关技术中人脸老化灵活程度低的问题。</p><p id=\"p0007\" number=\"0004\">本发明的技术方案如下：包括：</p><p id=\"p0008\" number=\"0005\">获得单元，用于获得原始输入图像<Image he=\"62\" wi=\"110\" file=\"BDA0004157173810000011.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>参考图像/&gt;<Image he=\"79\" wi=\"80\" file=\"BDA0004157173810000012.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和预先定义的老化文本t<Sup>ref</Sup>；</p><p id=\"p0009\" number=\"0006\">CLIP编码器，用于将所述参考图像<Image he=\"82\" wi=\"84\" file=\"BDA0004157173810000013.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和所述老化文本t<Sup>ref</Sup>映射到CLIP隐空间，分别得到隐向量e<Sup>img</Sup>和隐向量e<Sup>txt</Sup>；</p><p id=\"p0010\" number=\"0007\">概率年龄预测单元，用文本先验N(e<Sup>txt</Sup>，I)做KL散度约束，根据隐向量e<Sup>img</Sup>得到老化条件的概率生成表示e<Sup>age</Sup>＝N(μ<Sub>φ</Sub>(e<Sup>img</Sup>)，σ<Sub>φ</Sub><Sup>2</Sup>(e<Sup>img</Sup>)I)；其中N(0，I)表示正态分布，μ<Sub>φ</Sub>表示正态分布的均值，σ<Sub>φ</Sub>表示正态分布的方差，φ为网络参数；</p><p id=\"p0011\" number=\"0008\">扩散自编码器，用于将原始输入图像<Image he=\"71\" wi=\"85\" file=\"BDA0004157173810000014.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>编码成语义条件/&gt;<Image he=\"74\" wi=\"363\" file=\"BDA0004157173810000015.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image></p><p id=\"p0012\" number=\"0009\">第一扩散解码器，用于将语义条件z<Sup>src</Sup>、预训练扩散自编码器中扩散第t步的加噪图像<Image he=\"76\" wi=\"87\" file=\"BDA0004157173810000016.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和老化条件e<Sup>age</Sup>解码解成去噪老化编辑后的图像p。</p><p id=\"p0013\" number=\"0010\">本发明的工作原理及有益效果为：</p><p id=\"p0014\" number=\"0011\">由于图像和文字作为老化条件更符合人类的直觉和认知，本发明首先将参考图像<Image he=\"77\" wi=\"78\" file=\"BDA0004157173810000021.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和预先定义的老化文本t<Sup>ref</Sup>经过预训练CLIP编码器映射到CLIP隐空间，分别得到相应表示为e<Sup>img</Sup>和e<Sup>txt</Sup>，利用CLIP隐空间文本和图像的高度一致对齐的特性；然后将老化条件视为从概率分布中的采样结果，并用文本先验做KL散度约束，对老化条件e<Sup>age</Sup>做概率生成表示，实现图像和文本灵活交互的老化条件生成。</p></Disclosure><DrawingsDescription id=\"description-of-drawings001\"><p id=\"p0015\">附图说明</p><p id=\"p0016\" number=\"0012\">下面结合附图和具体实施方式对本发明作进一步详细的说明。</p><p id=\"p0017\" number=\"0013\">图1为本发明原理框图；</p><p id=\"p0018\" number=\"0014\">图2为本发明中概率年龄预测单元原理图；</p><p id=\"p0019\" number=\"0015\">图3为本发明中扩散自编码器原理图；</p><p id=\"p0020\" number=\"0016\">图4为本发明中自适应调制模块原理图。</p></DrawingsDescription><InventionMode id=\"mode-for-invention001\"><p id=\"p0021\">具体实施方式</p><p id=\"p0022\" number=\"0017\">下面将结合本发明实施例，对本发明实施例中的技术方案进行清楚、完整地描述，显然，所描述的实施例仅仅是本发明一部分实施例，而不是全部的实施例。基于本发明中的实施例，本领域普通技术人员在没有作出创造性劳动前提下所获得的所有其他实施例，都涉及本发明保护的范围。</p><p id=\"p0023\" number=\"0018\">如图1所示，本实施例灵活多样的人脸老化系统包括：</p><p id=\"p0024\" number=\"0019\">获得单元，用于获得原始输入图像<Image he=\"63\" wi=\"101\" file=\"BDA0004157173810000022.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>参考图像/&gt;<Image he=\"77\" wi=\"79\" file=\"BDA0004157173810000023.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和预先定义的老化文本t<Sup>ref</Sup>；</p><p id=\"p0025\" number=\"0020\">CLIP编码器，用于将所述参考图像<Image he=\"81\" wi=\"82\" file=\"BDA0004157173810000024.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和所述老化文本t<Sup>ref</Sup>映射到CLIP隐空间，分别得到隐向量e<Sup>img</Sup>和隐向量e<Sup>txt</Sup>；</p><p id=\"p0026\" number=\"0021\">概率年龄预测单元，用文本先验N(e<Sup>txt</Sup>，I)做KL散度约束，根据隐向量e<Sup>img</Sup>得到老化条件的概率生成表示e<Sup>age</Sup>＝N(μ<Sub>φ</Sub>(e<Sup>img</Sup>)，σ<Sub>φ</Sub><Sup>2</Sup>(e<Sup>img</Sup>)I)；其中N(0，I)表示正态分布，μ<Sub>φ</Sub>表示正态分布的均值，σ<Sub>φ</Sub>表示正态分布的方差，φ为预测单元的网络参数；</p><p id=\"p0027\" number=\"0022\">扩散自编码器，用于将原始输入图像<Image he=\"70\" wi=\"78\" file=\"BDA0004157173810000031.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>编码成语义条件/&gt;<Image he=\"78\" wi=\"361\" file=\"BDA0004157173810000032.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image></p><p id=\"p0028\" number=\"0023\">第一扩散解码器，用于将语义条件z<Sup>src</Sup>、预训练扩散自编码器中扩散第t步的加噪图像<Image he=\"79\" wi=\"86\" file=\"BDA0004157173810000033.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和老化条件e<Sup>age</Sup>解码解成去噪老化编辑后的图像p。</p><p id=\"p0029\" number=\"0024\">本实施例首先将输入的参考图像<Image he=\"77\" wi=\"80\" file=\"BDA0004157173810000034.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>和预先定义的老化文本t<Sup>ref</Sup>经过预训练CLIP编码器映射到CLIP隐空间，分别得到相应表示为隐向量e<Sup>img</Sup>和隐向量e<Sup>txt</Sup>，利用CLIP隐空间文本和图像的高度一致对齐的特性，设计轻量的网络结构对老化条件做概率生成表示，如图2所示。轻量的网络结构首先包括第二多层感知机MLP，通过将隐向量e<Sup>img</Sup>输入第二多层感知机MLP得到均值μ<Sub>φ</Sub>(e<Sup>img</Sup>)和方差σ<Sub>φ</Sub>(e<Sup>img</Sup>)，将老化条件视为从概率分布中的采样结果：e<Sup>age</Sup>＝N(μ<Sub>φ</Sub>(e<Sup>img</Sup>)，σ<Sub>φ</Sub><Sup>2</Sup>(e<Sup>img</Sup>)I)，并用文本先验N(e<Sup>txt</Sup>，I)做KL散度约束。然后采用预训练扩散自编码器中的语义编码器将原始输入图像/&gt;<Image he=\"62\" wi=\"79\" file=\"BDA0004157173810000035.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>编码成语义条件z<Sup>src</Sup>，语义条件中包含图像的主体感知特征。最后结合语义条件z<Sup>src</Sup>和老化编码器得到的年龄条件z<Sup>age</Sup>，令第一扩散解码器不断执行(共T步)反向过程得到老化编辑后的图像p(x<Sub>t-1</Sub>|x<Sub>t</Sub>，z<Sup>tar</Sup>，t)，其中p(x<Sub>T</Sub>)＝N(0，I)，z<Sup>tar</Sup>＝z<Sup>src</Sup>+z<Sup>age</Sup>。其中，第一扩散解码器采用预训练的扩散解码器。</p><p id=\"p0030\" number=\"0025\">近年来，DDPM(Denoising Diffusion Probabilistic Models,去噪扩散概率模型)的提出大大促进了图像生成领域的发展。DDPM通过一个前向加噪过程(Forward)和一个反向去噪过程(Denoise)来进行训练。在前向加噪过程中，DDPM不断向图像中加入固定参数的噪声至图像近似于一个随机高斯噪声，而在反向过程中训练网络不断预测噪声并做降噪恢复原始图像。训练完毕后，DDPM可以从高斯噪声中任意采样来做图像生成。本实施例中预训练扩散自编码器在训练DDPM的基础上额外训练了一个语义编码器(Semantic Encoder)，在反向过程中将语义编码器的编码特征作为条件进行训练，其结构图如图3所示。</p><p id=\"p0031\" number=\"0026\">需要说明的是，在训练和采样的过程中，也可以利用文本先验，对文本向量做小的扰动，直接得到e<Sup>age</Sup>＝e<Sup>txt</Sup>+σ·η,η～N(0，I)，通过这种方式有利于模型学习到如何同时利用文本和图像信息指导老化生成，让老化条件的生成更加灵活。</p><p id=\"p0032\" number=\"0027\">进一步，自适应调制模块，用于将老化条件e<Sup>age</Sup>和语义条件z<Sup>src</Sup>进行自适应融合，得到年龄条件z<Sup>age</Sup>，用年龄条件z<Sup>age</Sup>代替老化条件e<Sup>age</Sup>，输入到解码器中得到老化编辑后的图像p；所述自适应融合的步骤具体包括：</p><p id=\"p0033\" number=\"0028\">利用多层感知机MLP将老化条件e<Sup>age</Sup>映射到扩散自编码器隐空间，得到映射向量Δz<Sup>age</Sup>；</p><p id=\"p0034\" number=\"0029\">分别利用两层全连接层从映射向量Δz<Sup>age</Sup>学习权重参数γ<Sub>θ</Sub>和β<Sub>θ</Sub>，通过自适应编码的方式与语义条件z<Sup>src</Sup>融合，得到年龄条件z<Sup>age</Sup>。</p><p id=\"p0035\" number=\"0030\">本实施例中，通过采用自适应调制的方式将CLIP隐空间中的e<Sup>age</Sup>映射到预训练扩散自编码器的隐空间，从而将CLIP隐空间中的e<Sup>age</Sup>与语义条件z<Sup>src</Sup>进行自适应融合，进一步得到多样化的老化条件z<Sup>age</Sup>。</p><p id=\"p0036\" number=\"0031\">进一步，还包括：</p><p id=\"p0037\" number=\"0032\">计算单元，用于进行损失函数L的计算，具体地，本实施例提出六种损失约束模型的训练，即L＝L<Sub>tKL</Sub>+λ<Sub>1</Sub>L<Sub>age</Sub>+λ<Sub>2</Sub>L<Sub>clip</Sub>+λ<Sub>3</Sub>L<Sub>id</Sub>+λ<Sub>4</Sub>L<Sub>norm</Sub>+λ<Sub>5</Sub>L<Sub>rec</Sub>。</p><p id=\"p0038\" number=\"0033\">本实施例中，利用预训练的扩散自编码器中的条件扩散解码器(DiffusionDecoder)执行去噪过程，在z<Sup>src</Sup>的指导下重构出原始输入图像<Image he=\"67\" wi=\"108\" file=\"BDA0004157173810000041.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>同理我们可以重构出原始输入图像/&gt;<Image he=\"76\" wi=\"114\" file=\"BDA0004157173810000042.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image></p><p id=\"p0039\" number=\"0034\">为了让得到的老化条件满足文本先验，避免坍塌到固定值，我们提出了文本引导的KL散度约束：L<Sub>tKL</Sub>＝D<Sub>KL</Sub>(N(μ<Sub>φ</Sub>(e<Sup>img</Sup>)，σ<Sub>φ</Sub><Sup>2</Sup>(e<Sup>img</Sup>)I)||N(e<Sup>txt</Sup>，I))</p><p id=\"p0040\" number=\"0035\">在实际训练过程中，在CLIP的隐空间为一个超球体的前提下，欧式距离的约束和负余弦相似度的约束是等价的，所以我们将KL散度中的距离项弱化为负余弦相似度和模值约束。</p><p id=\"p0041\" number=\"0036\">为了保证老化结果满足目标年龄条件，我们提出了两种损失：年龄特征对比损失L<Sub>age</Sub>和CLIP方向损失L<Sub>clip</Sub>。其中年龄损失L<Sub>age</Sub>是在预训练的年龄估计器f(·)的特征空间对中间重构结果<Image he=\"78\" wi=\"330\" file=\"BDA0004157173810000043.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"yes\"></Image>做余弦相似度＜·＞的对比损失，具体定义如下：</p><p id=\"p0042\" number=\"0037\"><Image he=\"64\" wi=\"1000\" file=\"BDA0004157173810000044.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"no\"></Image></p><p id=\"p0043\" number=\"0038\">由于扩散中间重构结果过于模糊，采用常规的L<Sub>2</Sub>损失容易导致年龄偏差较大，所以结合扩散模型的特性，我们提出年龄对比损失保证生成结果年龄的一致性，实验中我们选取m为0.25。同时，为了避免单一年龄估计器引入的年龄偏差，我们利用预训练大模型CLIP做额外的年龄监督指导：</p><p id=\"p0044\" number=\"0039\"><Image he=\"140\" wi=\"539\" file=\"BDA0004157173810000045.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"no\"></Image></p><p id=\"p0045\" number=\"0040\"><Image he=\"95\" wi=\"590\" file=\"BDA0004157173810000051.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"no\"></Image></p><p id=\"p0046\" number=\"0041\">ΔT＝E<Sub>txt</Sub>(t<Sup>ref</Sup>)-E<Sub>txt</Sub>(t<Sup>src</Sup>)</p><p id=\"p0047\" number=\"0042\">其中E<Sub>txt</Sub>(·)和E<Sub>img</Sub>(·)表示预训练的CLIP编码器，实验中t<Sup>src</Sup>我们选取的文本表述为“a face”。</p><p id=\"p0048\" number=\"0043\">为了提高生成图像的质量与年龄无关特征的保持性，我们提出了L<Sub>id</Sub>，L<Sub>norm</Sub>和L<Sub>rec</Sub>，具体来说，我们采用预训练的人脸识别模型R(·)，保证模型在老化过程中身份特征不变：</p><p id=\"p0049\" number=\"0044\"><Image he=\"71\" wi=\"555\" file=\"BDA0004157173810000052.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"no\"></Image></p><p id=\"p0050\" number=\"0045\">其中&lt;·&gt;表示余弦相似度的计算，R(·)为预训练人脸识别模型特征空间的特征表示。为了保证生成质量，我们对多样性老化条件提出了正则项损失L<Sub>norm</Sub>,定义如下：</p><p id=\"p0051\" number=\"0046\">L<Sub>norm</Sub>＝||z<Sup>age</Sup>||<Sub>2</Sub></p><p id=\"p0052\" number=\"0047\">为了保证模型的年龄无关特征保持不变，在训练过程中我们随机令参考图像和输入图像一致，并用L<Sub>1</Sub>做约束：</p><p id=\"p0053\" number=\"0048\"><Image he=\"112\" wi=\"418\" file=\"BDA0004157173810000053.GIF\" imgContent=\"drawing\" imgFormat=\"GIF\" orientation=\"portrait\" inline=\"no\"></Image></p><p id=\"p0054\" number=\"0049\">我们的整体目标函数可以总结如下：</p><p id=\"p0055\" number=\"0050\">L＝L<Sub>tKL</Sub>+λ<Sub>1</Sub>L<Sub>age</Sub>+λ<Sub>2</Sub>L<Sub>clip</Sub>+λ<Sub>3</Sub>L<Sub>id</Sub>+λ<Sub>4</Sub>L<Sub>norm</Sub>+λ<Sub>5</Sub>L<Sub>rec</Sub></p><p id=\"p0056\" number=\"0051\">其中，λ<Sub>i</Sub>是各损失的权重。</p><p id=\"p0057\" number=\"0052\">以上仅为本发明的较佳实施例而已，并不用以限制本发明，凡在本发明的精神和原则之内，所作的任何修改、等同替换、改进等，均应包含在本发明的保护范围之内。</p></InventionMode>";
        txt = "<InventionTitle id=\"title1\">灵活多样的人脸图像老化生成系统 InventionTitle <a>some</a></InventionTitle>";
        txt = "<InventionTitle id=\"title1\" class=\"oldclass\">灵活多样的人脸图像老化生成系统 InventionTitle <a>some</a></InventionTitle><technical-field>\n" +
                "      <p id=\"p-00001-zh\" num=\"0001\">技术领域</p>\n" +
                "      <p id=\"p-00002-zh\" num=\"0002\">本发明涉及农业机械领域，具体是一种手扶拖拉机配套农机具适应在农田垄上作业的双尾轮行走机构。</p>\n" +
                "    </technical-field><heading>进模压</heading> /> /> <br /><br />";
        Document doc = Jsoup.parse(txt);
        changeNodeNameWithClass(doc, "InventionTitle", "p","instructions-title");
        changeNodeNameWithClass(doc, "technical-field", "div", "instructions-title");
        changeNodeNameWithClass(doc, "heading", "h3", "");
        changeNodeNameWithClass(doc, "br", "p", "");
        changeNodeNameIfTxt(doc, "p", "h3", "技术领域");
        String newTxt = doc.body().html();
        newTxt = newTxt.replace("/&gt;", "");

        System.out.println(newTxt);
    }

    /**
     * 修改节点名称
     *
     * @param oldName
     * @param newName
     */
    public static void changeNodeName(Document doc, String oldName, String newName) {
        // 查找所有的 InventionTitle 元素
        Elements inventionTitles = doc.select(oldName);

        // 遍历并替换每个 InventionTitle 元素
        for (Element inventionTitle : inventionTitles) {
            // 创建一个新的 p 元素
            Element pElement = doc.createElement(newName);
            for (Attribute attr : inventionTitle.attributes().asList()) {
                pElement.attr(attr.getKey(), attr.getValue());
            }

            // 将原有的文本内容添加到新的 <p> 元素中
            pElement.html(inventionTitle.html());

            // 替换旧元素
            inventionTitle.replaceWith(pElement);
        }
    }

    /**
     * 修改节点名称并添加class
     *
     * @param oldName
     * @param newName
     */
    public static void changeNodeNameWithClass(Document doc, String oldName, String newName, String className) {
        // 查找所有的 InventionTitle 元素
        Elements inventionTitles = doc.select(oldName);

        // 遍历并替换每个 InventionTitle 元素
        for (Element inventionTitle : inventionTitles) {
            // 创建一个新的 p 元素
            Element pElement = doc.createElement(newName);
            for (Attribute attr : inventionTitle.attributes().asList()) {
                pElement.attr(attr.getKey(), attr.getValue());
            }
            if (StringUtils.isNotBlank(className)) {
                pElement.addClass(className);
            }

            // 将原有的文本内容添加到新的 <p> 元素中
            pElement.html(inventionTitle.html());

            // 替换旧元素
            inventionTitle.replaceWith(pElement);
        }
    }


    /**
     * 如果文本达到要求则修改节点名称
     *
     * @param oldName
     * @param newName
     */
    public static void changeNodeNameIfTxt(Document doc, String oldName, String newName, String oldElementTxt) {
        // 查找所有的 xx 元素
        Elements inventionTitles = doc.select(oldName);

        // 遍历并替换每个 xxx 元素
        for (Element inventionTitle : inventionTitles) {
            String txt = inventionTitle.text().trim();
            if (!txt.equals(oldElementTxt)) {
                continue;
            }

            // 创建一个新的 xxxx 元素
            Element pElement = doc.createElement(newName);
            for (Attribute attr : inventionTitle.attributes().asList()) {
                pElement.attr(attr.getKey(), attr.getValue());
            }

            // 将原有的文本内容添加到新的 <p> 元素中
            pElement.html(inventionTitle.html());

            // 替换旧元素
            inventionTitle.replaceWith(pElement);
        }
    }
}
