

Spring MVC 默认采用 Jackson 解析 Json，尽管还有一些其它同样优秀的 json 解析工具，例如 Fast Json、GSON，但是出于最小依赖的考虑，也许 Json 解析第一选择就应该是 Jackson。

一、简介
====

Jackson 被誉为“Java JSON 库”或“Java 最佳 JSON 解析器”。或者简单地称为“Java 的 JSON”。

不仅如此，Jackson 是一套用于 Java（和 JVM 平台）的数据处理工具，包括旗舰级流式[JSON](https://en.wikipedia.org/wiki/JSON)解析器/生成器库、匹配的数据绑定库（与 JSON 之间的 POJO）以及附加的数据格式模块处理以 [Avro](https://github.com/FasterXML/jackson-dataformats-binary/blob/master/avro)、 [BSON](https://github.com/michel-kraemer/bson4jackson)、 [CBOR](https://github.com/FasterXML/jackson-dataformats-binary/blob/master/cbor)、 [CSV](https://github.com/FasterXML/jackson-dataformats-text/blob/master/csv)、 [Smile](https://github.com/FasterXML/jackson-dataformats-binary/tree/master/smile)、 [(Java) Properties](https://github.com/FasterXML/jackson-dataformats-text/blob/master/properties)、 [Protobuf](https://github.com/FasterXML/jackson-dataformats-binary/tree/master/protobuf)、 [TOML](https://github.com/FasterXML/jackson-dataformats-text/blob/2.13/toml)、 [XML](https://github.com/FasterXML/jackson-dataformat-xml) 或[YAML](https://github.com/FasterXML/jackson-dataformats-text/blob/master/yaml)编码的数据；甚至还有大量的数据格式模块来支持广泛使用的数据类型的数据类型，例如 [Guava](https://github.com/FasterXML/jackson-datatypes-collections)、 [Joda](https://github.com/FasterXML/jackson-datatype-joda)、 [PCollections](https://github.com/FasterXML/jackson-datatypes-collections) 还有很多很多（见下文）。

虽然实际的核心组件位于他们自己的项目下——包括三个核心包（[streaming](https://github.com/FasterXML/jackson-core)、[databind](https://github.com/FasterXML/jackson-databind)、[annotations](https://github.com/FasterXML/jackson-annotations)）；数据格式库；数据类型库；[JAX-RS 提供商](https://github.com/FasterXML/jackson-jaxrs-providers)；以及一系列其他扩展模块——该项目充当将所有部分连接在一起的中心枢纽。



Jackson 是当前用的比较广泛的，用来序列化和反序列化 json 的 Java 的开源框架。Jackson 社区相对比较活跃，更新速度也比较快， 从 Github 中的统计来看，Jackson 是最流行的 json 解析器之一 。 Spring MVC 的默认 json 解析器便是 Jackson。 Jackson 优点很多。 Jackson 所依赖的 jar 包较少 ，简单易用。与其他 Java 的 json 的框架 Gson 等相比， Jackson 解析大的 json 文件速度比较快；Jackson 运行时占用内存比较低，性能比较好；Jackson 有灵活的 API，可以很容易进行扩展和定制。

Jackson 的 1.x 版本的包名是 org.codehaus.jackson ，当升级到 2.x 版本时，包名变为 com.fasterxml.jackson。

Jackson 的核心模块由三部分组成。

*   jackson-core，核心包，提供基于 "流模式" 解析的相关 API，它包括 JsonPaser 和 JsonGenerator。 Jackson 内部实现正是通过高性能的流模式 API 的 JsonGenerator 和 JsonParser 来生成和解析 json。
*   jackson-annotations，注解包，提供标准注解功能；
*   jackson-databind ，数据绑定包， 提供基于 "对象绑定" 解析的相关 API （ ObjectMapper ） 和 "树模型" 解析的相关 API （JsonNode）；基于 "对象绑定" 解析的 API 和 "树模型" 解析的 API 依赖基于 "流模式" 解析的 API。

源码地址：[FasterXML/jackson](https://github.com/FasterXML/jackson)

二、依赖
====

使用 Maven 构建项目，需要添加依赖：

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <version>2.9.6</version>
</dependency>

<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-annotations</artifactId>
  <version>2.9.6</version>
</dependency>

<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.6</version>
</dependency>
```

当然了，jackson-databind 依赖 jackson-core 和 jackson-annotations，所以可以只显示地添加 jackson-databind 依赖，jackson-core 和 jackson-annotations 也随之添加到 Java 项目工程中。

```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.9.6</version>
</dependency>
```

下面是 Jackson 的用法。

三、 ObjectMapper
===============

Jackson 最常用的 API 就是基于 "对象绑定" 的 ObjectMapper：

*   ObjectMapper 可以从字符串，流或文件中解析 JSON，并创建表示已解析的 JSON 的 Java 对象。 将 JSON 解析为 Java 对象也称为从 JSON 反序列化 Java 对象。
    
*   ObjectMapper 也可以从 Java 对象创建 JSON。 从 Java 对象生成 JSON 也称为将 Java 对象序列化为 JSON。
    
*   Object 映射器可以将 JSON 解析为自定义的类的对象，也可以解析置 JSON 树模型的对象。
    

之所以称为 ObjectMapper 是因为它将 JSON 映射到 Java 对象（反序列化），或者将 Java 对象映射到 JSON（序列化）。

一）、从 JSON 中获取 Java 对象
---------------------

### 1、简单示例

一个简单的例子：

Car 类：

```Java
public class Car {
 private String brand = null;
    private int doors = 0;

    public String getBrand() { return this.brand; }
    public void   setBrand(String brand){ this.brand = brand;}

    public int  getDoors() { return this.doors; }
    public void setDoors (int doors) { this.doors = doors; }
}
```

将 Json 转换为 Car 类对象：

```Java
ObjectMapper objectMapper = new ObjectMapper();

  String carJson ="{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

  try {
      Car car = objectMapper.readValue(carJson, Car.class);

      System.out.println("car brand = " + car.getBrand());
      System.out.println("car doors = " + car.getDoors());
  } catch (IOException e) {
      e.printStackTrace();
  }
```

### 2、 ObjectMapper 如何匹配 JSON 对象的字段和 Java 对象的属性

默认情况下，Jackson 通过将 JSON 字段的名称与 Java 对象中的 getter 和 setter 方法进行匹配，将 JSON 对象的字段映射到 Java 对象中的属性。 Jackson 删除了 getter 和 setter 方法名称的 “get” 和“ set”部分，并将其余名称的第一个字符转换为小写。

例如，名为 brand 的 JSON 字段与名为 getBrand() 和 setBrand() 的 Java getter 和 setter 方法匹配。 名为 engineNumber 的 JSON 字段将与名为 getEngineNumber() 和 setEngineNumber() 的 getter 和 setter 匹配。

如果需要以其他方式将 JSON 对象字段与 Java 对象字段匹配，则需要使用自定义序列化器和反序列化器，或者使用一些 Jackson 注解。

### 3、JSON 字符串 -->Java 对象

从 JSON 字符串读取 Java 对象非常容易。 上面已经有了一个示例——JSON 字符串作为第一个参数传递给 ObjectMapper 的 readValue() 方法。 这是另一个简单的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

String carJson =
    "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

Car car = objectMapper.readValue(carJson, Car.class);
```

### 3、JSON 字符输入流 -->Java 对象

还可以从通过 Reader 实例加载的 JSON 中读取对象。示例如下：

```java
ObjectMapper objectMapper = new ObjectMapper();

String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 4 }";
Reader reader = new StringReader(carJson);

Car car = objectMapper.readValue(reader, Car.class);
```

### 4、JSON 文件 -->Java 对象

从文件读取 JSON 当然可以通过 FileReader（而不是 StringReader）来完成，也可以通过 File 对象来完成。 这是从文件读取 JSON 的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

File file = new File("data/car.json");

Car car = objectMapper.readValue(file, Car.class);
```

### 5、JSON via URL--->Java 对象

可以通过 URL（java.net.URL）从 JSON 读取对象，如下所示：

```java
ObjectMapper objectMapper = new ObjectMapper();

URL url = new URL("file:data/car.json");

Car car = objectMapper.readValue(url, Car.class);
```

示例使用文件 URL，也可以使用 HTTP URL（类似于 http://jenkov.com/some-data.json）。

### 6、JSON 字节输入流 -->Java 对象

也可以使用 ObjectMapper 通过 InputStream 从 JSON 读取对象。 这是一个从 InputStream 读取 JSON 的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

InputStream input = new FileInputStream("data/car.json");

Car car = objectMapper.readValue(input, Car.class);
```

### 7、JSON 二进制数组 -->Java 对象

Jackson 还支持从 JSON 字节数组读取对象。 这是从 JSON 字节数组读取对象的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

byte[] bytes = carJson.getBytes("UTF-8");

Car car = objectMapper.readValue(bytes, Car.class);
```

### 8、JSON 数组字符串 -->Java 对象数组

Jackson ObjectMapper 也可以从 JSON 数组字符串读取对象数组。 这是从 JSON 数组字符串读取对象数组的示例：

```java
String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

ObjectMapper objectMapper = new ObjectMapper();

Car[] cars2 = objectMapper.readValue(jsonArray, Car[].class);
```

需要将 Car 数组类作为第二个参数传递给 readValue() 方法。

读取对象数组还可以与字符串以外的其他 JSON 源一起使用。 例如，文件，URL，InputStream，Reader 等。

### 9、JSON 数组字符串 -->List

Jackson ObjectMapper 还可以从 JSON 数组字符串读取对象的 Java List。 这是从 JSON 数组字符串读取对象列表的示例：

```java
String jsonArray = "[{\"brand\":\"ford\"}, {\"brand\":\"Fiat\"}]";

 ObjectMapper objectMapper = new ObjectMapper();

 List<Car> cars1 = objectMapper.readValue(jsonArray, new TypeReference<List<Car>>(){});
```

### 10、JSON 字符串 -->Map

Jackson ObjectMapper 还可以从 JSON 字符串读取 Java Map。 如果事先不知道将要解析的确切 JSON 结构，这种方法是很有用的。 通常，会将 JSON 对象读入 Java Map。 JSON 对象中的每个字段都将成为 Java Map 中的键，值对。

这是一个使用 Jackson ObjectMapper 从 JSON 字符串读取 Java Map 的示例：

```java
String jsonObject = "{\"brand\":\"ford\", \"doors\":5}";

ObjectMapper objectMapper = new ObjectMapper();
Map<String, Object> jsonMap = objectMapper.readValue(jsonObject,
    new TypeReference<Map<String,Object>>(){});
```

### 11、忽略未知的 JSON 字段

有时候，与要从 JSON 读取的 Java 对象相比，JSON 中的字段更多。 默认情况下，Jackson 在这种情况下会抛出异常，报不知道 XYZ 字段异常，因为在 Java 对象中找不到该字段。

但是，有时应该允许 JSON 中的字段多于相应的 Java 对象中的字段。 例如，要从 REST 服务解析 JSON，而该 REST 服务包含的数据远远超出所需的。 在这种情况下，可以使用 Jackson 配置忽略这些额外的字段。 以下是配置 Jackson ObjectMapper 忽略未知字段的示例：

```java
objectMapper.configure(
    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

### 12、不允许基本类型为 null

如果 JSON 字符串包含其值设置为 null 的字段（对于在相应的 Java 对象中是基本数据类型（int，long，float，double 等）的字段），Jackson ObjectMapper 默认会处理基本数据类型为 null 的情况，我们可以可以将 Jackson ObjectMapper 默认配置为失效，这样基本数据为 null 就会转换失败。 例如以下 Car 类：

```java
public class Car {
    private String brand = null;
    private int doors = 0;

    public String getBrand() { return this.brand; }
    public void   setBrand(String brand){ this.brand = brand;}

    public int  getDoors(){ return this.doors; }
    public void setDoors (int doors) { this.doors = doors; }
}
```

doors 字段是一个 int 类型，它是 Java 中的基本数据类型。

现在，假设有一个与 Car 对象相对应的 JSON 字符串，如下所示：

```java
{ "brand":"Toyota", "doors":null }
```

请注意，doors 字段值为 null。 Java 中的基本数据类型不能为 null 值。 默认情况下，Jackson ObjectMapper 会忽略原始字段的空值。 但是，可以将 Jackson ObjectMapper 配置设置为失败。

```java
ObjectMapper objectMapper = new ObjectMapper();

objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
```

在 FAIL_ON_NULL_FOR_PRIMITIVES 配置值设置为 true 的情况下，尝试将空 JSON 字段解析为基本类型 Java 字段时会遇到异常。 这是一个 Java Jackson ObjectMapper 示例，该示例将失败，因为 JSON 字段包含原始 Java 字段的空值：

```java
ObjectMapper objectMapper = new ObjectMapper();

  objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

  String carJson = "{ \"brand\":\"Toyota\", \"doors\":null }";

  Car car = objectMapper.readValue(carJson, Car.class);
```

结果：

![](./image/教程-Jackson 使用详解/17242249d91773e2tplv-t2oaga2asx-jj-mark1890000q75-1692027916727-7.avif)  

### 13、自定义反序列化

有时，可能希望以不同于 Jackson ObjectMapper 缺省方式的方式将 JSON 字符串读入 Java 对象。 可以将自定义反序列化器添加到 ObjectMapper，可以按需要执行反序列化。

这是在 Jackson 的 ObjectMapper 中注册和使用自定义反序列化器的方式：

```java
String json = "{ \"brand\" : \"Ford\", \"doors\" : 6 }";

  SimpleModule module =
          new SimpleModule("CarDeserializer", new Version(3, 1, 8, null, null, null));
  module.addDeserializer(Car.class, new CarDeserializer(Car.class));

  ObjectMapper mapper = new ObjectMapper();
  mapper.registerModule(module);

  Car car = mapper.readValue(json, Car.class);
```

自定义反序列化器 CarDeserializer 类：

```java
public class CarDeserializer extends StdDeserializer<Car> {

    public CarDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Car deserialize(JsonParser parser, DeserializationContext deserializer) throws IOException {
        Car car = new Car();
        while(!parser.isClosed()){
            JsonToken jsonToken = parser.nextToken();

            if(JsonToken.FIELD_NAME.equals(jsonToken)){
                String fieldName = parser.getCurrentName();
                System.out.println(fieldName);

                jsonToken = parser.nextToken();

                if("brand".equals(fieldName)){
                    car.setBrand(parser.getValueAsString());
                } else if ("doors".equals(fieldName)){
                    car.setDoors(parser.getValueAsInt());
                }
            }
        }
        return car;
    }
}
```

二）、将对象写入 JSON
-------------

### 1、Java 对象 -->JSON

Jackson ObjectMapper 也可以用于从对象生成 JSON。 可以使用以下方法之一进行操作：

*   writeValue()
*   writeValueAsString()
*   writeValueAsBytes()

这是一个从 Car 对象生成 JSON 的示例，和上面的实例相反：

```java
ObjectMapper objectMapper = new ObjectMapper();

  Car car = new Car();
  car.setBrand("BMW");
  car.setDoors(4);

  objectMapper.writeValue(
      new FileOutputStream("data/output-2.json"), car);
```

此示例首先创建一个 ObjectMapper，然后创建一个 Car 实例，最后调用 ObjectMapper 的 writeValue() 方法，该方法将 Car 对象转换为 JSON 并将其写入给定的 FileOutputStream。

ObjectMapper 的 writeValueAsString() 和 writeValueAsBytes() 都从一个对象生成 JSON，并将生成的 JSON 作为 String 或字节数组返回。 示例如下：

```java
ObjectMapper objectMapper = new ObjectMapper();

  Car car = new Car();
  car.setBrand("宝马");
  car.setDoors(4);

  String json = objectMapper.writeValueAsString(car);
  System.out.println(json);
```

运行结果： ![](./image/教程-Jackson 使用详解/17242249d91c78d5tplv-t2oaga2asx-jj-mark1890000q75.avif)  

### 2、自定义序列化

有时，想要将 Java 对象序列化为 JSON 的方式与使用 Jackson 的默认方式不同。 例如，可能想要在 JSON 中使用与 Java 对象中不同的字段名称，或者希望完全省略某些字段。

Jackson 可以在 ObjectMapper 上设置自定义序列化器。 该序列化器已为某个类注册，然后在每次要求 ObjectMapper 序列化 Car 对象时将调用该序列化器。

这是为 Car 类注册自定义序列化器的示例：

```java
CarSerializer carSerializer = new CarSerializer(Car.class);
  ObjectMapper objectMapper = new ObjectMapper();

  SimpleModule module =
          new SimpleModule("CarSerializer", new Version(2, 1, 3, null, null, null));
  module.addSerializer(Car.class, carSerializer);

  objectMapper.registerModule(module);

  Car car = new Car();
  car.setBrand("Mercedes");
  car.setDoors(5);

  String carJson = objectMapper.writeValueAsString(car);
```

自定义序列化器 CarSerializer 类：

```java
public class CarSerializer extends StdSerializer<Car> {

    protected CarSerializer(Class<Car> t) {
        super(t);
    }

    public void serialize(Car car, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("producer", car.getBrand());
        jsonGenerator.writeNumberField("doorCount", car.getDoors());
        jsonGenerator.writeEndObject();
    }
}
```

运行结果：

![](./image/教程-Jackson 使用详解/17242249db99a96btplv-t2oaga2asx-jj-mark1890000q75.avif)

三）、Jackson 日期转化
---------------

默认情况下，Jackson 会将 java.util.Date 对象序列化为其 long 型的值，该值是自 1970 年 1 月 1 日以来的毫秒数。但是，Jackson 还支持将日期格式化为字符串。

### 1、Date-->long

默认的 Jackson 日期格式，该格式将 Date 序列化为自 1970 年 1 月 1 日以来的毫秒数（long 类型）。

这是一个包含 Date 字段的 Java 类示例：

```java
private String type = null;
    private Date date = null;

    public Transaction() {
    }

    public Transaction(String type, Date date) {
        this.type = type;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
```

就像使用其他 Java 对象进行序列化一样，代码如下：

```java
Transaction transaction = new Transaction("transfer", new Date());

  ObjectMapper objectMapper = new ObjectMapper();
  String output = objectMapper.writeValueAsString(transaction);

  System.out.println(output);
```

运行结果：

![](./image/教程-Jackson 使用详解/17242249d868123atplv-t2oaga2asx-jj-mark1890000q75.avif)  

### 2、Date-->String

日期的 long 序列化格式不符合人类的时间查看格式。 因此，Jackson 也支持文本日期格式。 可以通过在 ObjectMapper 上设置 SimpleDateFormat 来指定要使用的确切 Jackson 日期格式。 这是在 Jackson 的 ObjectMapper 上设置 SimpleDateFormat 的示例：

```java
Transaction transaction = new Transaction("transfer", new Date());

  ObjectMapper objectMapper = new ObjectMapper();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  objectMapper.setDateFormat(dateFormat);

  String output2 = objectMapper.writeValueAsString(transaction);
  System.out.println(output2);
```

运行结果：

![](./image/教程-Jackson 使用详解/17242249db453b83tplv-t2oaga2asx-jj-mark1890000q75.avif)  

四）、Jackson JSON 树模型
-------------------

Jackson 具有内置的树模型，可用于表示 JSON 对象。 如果不知道接收到的 JSON 的格式，或者由于某种原因而不能（或者只是不想）创建一个类来表示它，那么就要用到 Jackson 的树模型。 如果需要在使用或转化 JSON 之前对其进行操作，也需要被用到 Jackson 树模型。 所有这些情况在数据流场景中都很常见。

Jackson 树模型由 JsonNode 类表示。 您可以使用 Jackson ObjectMapper 将 JSON 解析为 JsonNode 树模型，就像使用您自己的类一样。

以下将展示如何使用 Jackson ObjectMapper 读写 JsonNode 实例。

### 1、Jackson Tree Model 简单例子

下面是一个简单的例子：

```java
String carJson =
          "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

  ObjectMapper objectMapper = new ObjectMapper();

  try {

      JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);

  } catch (IOException e) {
      e.printStackTrace();
  }
```

只需将 JsonNode.class 作为第二个参数传递给 readValue() 方法，而不是本教程前面的示例中使用的 Car.class，就可以将 JSON 字符串解析为 JsonNode 对象而不是 Car 对象。 。

ObjectMapper 类还具有一个特殊的 readTree() 方法，该方法返回 JsonNode。 这是使用 ObjectMapper readTree() 方法将 JSON 解析为 JsonNode 的示例：

```java
String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

ObjectMapper objectMapper = new ObjectMapper();

try {

    JsonNode jsonNode = objectMapper.readTree(carJson);

} catch (IOException e) {
    e.printStackTrace();
}
```

### 2、Jackson JsonNode 类

通过 JsonNode 类，可以以非常灵活和动态的方式将 JSON 作为 Java 对象导航。这里了解一些如何使用它的基础知识。

将 JSON 解析为 JsonNode（或 JsonNode 实例树）后，就可以浏览 JsonNode 树模型。 这是一个 JsonNode 示例，显示了如何访问 JSON 字段，数组和嵌套对象：

```java
String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
        "  \"nestedObject\" : { \"field\" : \"value\" } }";

ObjectMapper objectMapper = new ObjectMapper();


try {

    JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);

    JsonNode brandNode = jsonNode.get("brand");
    String brand = brandNode.asText();
    System.out.println("brand = " + brand);

    JsonNode doorsNode = jsonNode.get("doors");
    int doors = doorsNode.asInt();
    System.out.println("doors = " + doors);

    JsonNode array = jsonNode.get("owners");
    JsonNode jsonNode = array.get(0);
    String john = jsonNode.asText();
    System.out.println("john  = " + john);

    JsonNode child = jsonNode.get("nestedObject");
    JsonNode childField = child.get("field");
    String field = childField.asText();
    System.out.println("field = " + field);

} catch (IOException e) {
    e.printStackTrace();
}
```

请注意，JSON 字符串现在包含一个称为 owners 的数组字段和一个称为 nestedObject 的嵌套对象字段。

无论访问的是字段，数组还是嵌套对象，都可以使用 JsonNode 类的 get() 方法。 通过将字符串作为参数提供给 get() 方法，可以访问 JsonNode 的字段。 如果 JsonNode 表示数组，则需要将索引传递给 get() 方法。 索引指定要获取的数组元素。

### 3、Java 对象 -->JsonNode

可以使用 Jackson ObjectMapper 将 Java 对象转换为 JsonNode，而 JsonNode 是转换后的 Java 对象的 JSON 表示形式。 可以通过 Jackson ObjectMapper valueToTree() 方法将 Java 对象转换为 JsonNode。 这是一个使用 ObjectMapper valueToTree() 方法将 Java 对象转换为 JsonNode 的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

Car car = new Car();
car.brand = "Cadillac";
car.doors = 4;

JsonNode carJsonNode = objectMapper.valueToTree(car);
```

### 4、JsonNode-->Java 对象

可以使用 Jackson ObjectMapper treeToValue() 方法将 JsonNode 转换为 Java 对象。 这类似于使用 Jackson Jackson 的 ObjectMapper 将 JSON 字符串（或其他来源）解析为 Java 对象。 唯一的区别是，JSON 源是 JsonNode。 这是一个使用 Jackson ObjectMapper treeToValue() 方法将 JsonNode 转换为 Java 对象的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

JsonNode carJsonNode = objectMapper.readTree(carJson);

Car car = objectMapper.treeToValue(carJsonNode);
```

上面的示例有点 “人为”，因为我们首先将 JSON 字符串转换为 JsonNode，然后将 JsonNode 转换为 Car 对象。 显然，如果我们有对原始 JSON 字符串的引用，则最好将其直接转换为 Car 对象，而无需先将其转换为 JsonNode。

四、JsonNode
==========

Jackson JsonNode 类 com.fasterxml.jackson.databind.JsonNode 是 Jackson 的 JSON 树形模型（对象图模型）。 Jackson 可以将 JSON 读取到 JsonNode 实例中，然后将 JsonNode 写入 JSON。 因此，这一节将说明如何将 JSON 反序列化为 JsonNode 以及将 JsonNode 序列化为 JSON。 此 Jackson JsonNode 教程还将说明如何从头开始构建 JsonNode 对象图，因此以后可以将它们序列化为 JSON。

1、JsonNode vs ObjectNode
------------------------

Jackson JsonNode 类是不可变的。 这意味着，实际上不能直接构建 JsonNode 实例的对象图。 而是创建 JsonNode 子类 ObjectNode 的对象图。 作为 JsonNode 的子类，可以在可以使用 JsonNode 的任何地方使用 ObjectNode。

2、JSON-->JsonNode
-----------------

要使用 Jackson 将 JSON 读取到 JsonNode 中，首先需要创建一个 Jackson ObjectMapper 实例。 在 ObjectMapper 实例上，调用 readTree() 并将 JSON 源作为参数传递。 这是将 JSON 反序列化为 JsonNode 的示例：

```java
String json = "{ \"f1\" : \"v1\" } ";

ObjectMapper objectMapper = new ObjectMapper();

JsonNode jsonNode = objectMapper.readTree(json);

System.out.println(jsonNode.get("f1").asText());
```

3、JsonNode-->JSON
-----------------

要将 Jackson 的 JsonNode 写入 JSON，还需要一个 Jackson ObjectMapper 实例。 在 ObjectMapper 上，调用 writeValueAsString() 方法或任何适合需要的写入方法。 这是将 JsonNode 写入 JSON 的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

JsonNode jsonNode = readJsonIntoJsonNode();

String json = objectMapper.writeValueAsString(jsonNode);
```

4、获取 JsonNode 字段
----------------

JsonNode 可以像 JSON 对象一样具有字段。 假设已将以下 JSON 解析为 JsonNode：

```java
{
    "field1" : "value1",
    "field2" : 999
}
```

此 JSON 对象具有两个名为 field1 和 field2 的字段。 如果有一个表示上述 JSON 对象的 Jackson JsonNode，则可以这样获得两个字段：

```java
JsonNode jsonNode = ... //parse above JSON into a JsonNode

JsonNode field1 = jsonNode.get("field1");
JsonNode field2 = jsonNode.get("field2");
```

请注意，即使两个字段都是 String 字段，get() 方法也始终返回 JsonNode 来表示该字段。

5、在路径中获取 JsonNode 字段
--------------------

Jackson JsonNode 有一个称为 at() 的特殊方法。 at() 方法可以从 JSON 图中以给定 JsonNode 为根的任何位置访问 JSON 字段。 假设 JSON 结构如下所示：

```java
{
  "identification" :  {
        "name" : "James",
        "ssn: "ABC123552"
    }
}
```

如果将此 JSON 解析为 JsonNode，则可以使用 at() 方法访问名称字段，如下所示：

```
JsonNode nameNode = jsonNode.at("/identification/name");
```

注意传递给 at() 方法的参数：字符串 / identification / name。 这是一个 JSON 路径表达式。 此路径表达式指定从根 JsonNode 到您要访问其值的字段的完整路径。 这类似于从文件系统根目录到 Unix 文件系统中文件的路径。

请注意，JSON 路径表达式必须以斜杠字符（/ 字符）开头。

at() 方法返回一个 JsonNode，它表示请求的 JSON 字段。 要获取该字段的实际值，需要调用下一部分介绍的方法之一。 如果没有节点与给定的路径表达式匹配，则将返回 null。

6、转换 JsonNode 字段
----------------

Jackson JsonNode 类包含一组可以将字段值转换为另一种数据类型的方法。 例如，将 String 字段值转换为 long 或相反。 这是将 JsonNode 字段转换为一些更常见的数据类型的示例：

```java
String f2Str = jsonNode.get("f2").asText();
double f2Dbl = jsonNode.get("f2").asDouble();
int    f2Int = jsonNode.get("f2").asInt();
long   f2Lng = jsonNode.get("f2").asLong();
```

**使用默认值转换:** 如果 JsonNode 中的字段可以为 null，则在尝试转换它时可以提供默认值。 这是使用默认值调用转换方法的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

String json = "{ \"f1\":\"Hello\", \"f2\":null }";

JsonNode jsonNode = objectMapper.readTree(json);

String f2Value = jsonNode.get("f2").asText("Default");
```

在示例的 JSON 字符串中可以看到，声明了 f2 字段，但将其设置为 null。 在这种情况下，调用 jsonNode.get（“f2”）。asText（“ Default”）将返回默认值，在此示例中为字符串 Default。

asDouble()，asInt() 和 asLong() 方法还可以采用默认参数值，如果尝试从中获取值的字段为 null，则将返回默认参数值。

请注意，如果该字段在 JSON 中未显式设置为 null，但在 JSON 中丢失，则调用 jsonNode.get（“fieldName”）将返回 Java null 值，您无法在该 Java 值上调用 asInt() ，asDouble()，asLong() 或 asText()。 如果尝试这样做，将会导致 NullPointerException。 这是说明这种情况的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

String json = "{ \"f1\":\"Hello\" }";

JsonNode jsonNode = objectMapper.readTree(json);

JsonNode f2FieldNode = jsonNode.get("f2");
```

7、创建一个 ObjectNode
-----------------

如前所述，JsonNode 类是不可变的。 要创建 JsonNode 对象图，必须能够更改图中的 JsonNode 实例，例如 设置属性值和子 JsonNode 实例等。由于是不可变的，因此无法直接使用 JsonNode 来实现。

而是创建一个 ObjectNode 实例，该实例是 JsonNode 的子类。 这是一个通过 Jackson ObjectMapper createObjectNode() 方法创建 ObjectNode 的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();

ObjectNode objectNode = objectMapper.createObjectNode();
```

8、Set ObjectNode 字段
-------------------

要在 Jackson ObjectNode 上设置字段，可以调用其 set() 方法，并将字段名称 String 和 JsonNode 作为参数传递。 这是在 Jackson 的 ObjectNode 上设置字段的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();
ObjectNode parentNode = objectMapper.createObjectNode();

JsonNode childNode = readJsonIntoJsonNode();

parentNode.set("child1", childNode);
```

9、Put ObjectNode 字段
-------------------

ObjectNode 类还具有一组方法，可以直接为字段 put(设置) 值。 这比尝试将原始值转换为 JsonNode 并使用 set() 进行设置要容易得多。 以下是使用 put() 方法为 ObjectNode 上的字段设置字符串值的示例：

```java
objectNode.put("field1", "value1");
objectNode.put("field2", 123);
objectNode.put("field3", 999.999);
```

10、删除字段
-------

ObjectNode 类具有一个称为 remove() 的方法，该方法可用于从 ObjectNode 中删除字段。 这是一个通过其 remove() 方法从 Jackson ObjectNode 删除字段的示例：

```java
objectNode.remove("fieldName");
```

11、循环 JsonNode 字段
-----------------

JsonNode 类具有一个名为 fieldNames() 的方法，该方法返回一个 Iterator，可以迭代 JsonNode 的所有字段名称。 我们可以使用字段名称来获取字段值。 这是一个迭代 Jackson JsonNode 的所有字段名称和值的示例：

```java
Iterator<String> fieldNames = jsonNode.fieldNames();

while(fieldNames.hasNext()) {
    String fieldName = fieldNames.next();

    JsonNode field = jsonNode.get(fieldName);
}
```

五、JsonParser
============

Jackson JsonParser 类是一个底层一些的 JSON 解析器。 它类似于 XML 的 Java StAX 解析器，差别是 JsonParser 解析 JSON 而不解析 XML。

Jackson JsonParser 的运行层级低于 Jackson ObjectMapper。 这使得 JsonParser 比 ObjectMapper 更快，但使用起来也比较麻烦。

1、创建一个 JsonParser
-----------------

为了创建 Jackson JsonParser，首先需要创建一个 JsonFactory。 JsonFactory 用于创建 JsonParser 实例。 JsonFactory 类包含几个 createParser() 方法，每个方法都使用不同的 JSON 源作为参数。

这是创建一个 JsonParser 来从字符串中解析 JSON 的示例：

```java
String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

JsonFactory factory = new JsonFactory();
JsonParser  parser  = factory.createParser(carJson);
```

2、用 JsonParser 转化 JSON
----------------------

一旦创建了 Jackson JsonParser，就可以使用它来解析 JSON。 JsonParser 的工作方式是将 JSON 分解为一系列令牌，可以一个一个地迭代令牌。

这是一个 JsonParser 示例，它简单地循环遍历所有标记并将它们输出到 System.out。 这是一个实际上很少用示例，只是展示了将 JSON 分解成的令牌，以及如何遍历令牌的基础知识。

```java
tring carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

JsonFactory factory = new JsonFactory();
JsonParser  parser  = factory.createParser(carJson);

while(!parser.isClosed()){
    JsonToken jsonToken = parser.nextToken();

    System.out.println("jsonToken = " + jsonToken);
}
```

只要 JsonParser 的 isClosed() 方法返回 false，那么 JSON 源中仍然会有更多的令牌。

可以使用 JsonParser 的 nextToken() 获得一个 JsonToken。 您可以使用此 JsonToken 实例检查给定的令牌。 令牌类型由 JsonToken 类中的一组常量表示。 这些常量是：

```java
START_OBJECT
END_OBJECT
START_ARRAY
END_ARRAY
FIELD_NAME
VALUE_EMBEDDED_OBJECT
VALUE_FALSE
VALUE_TRUE
VALUE_NULL
VALUE_STRING
VALUE_NUMBER_INT
VALUE_NUMBER_FLOAT
```

可以使用这些常量来找出当前 JsonToken 是什么类型的令牌。 可以通过这些常量的 equals() 方法进行操作。 这是一个例子：

```java
String carJson =
        "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";

JsonFactory factory = new JsonFactory();
JsonParser  parser  = factory.createParser(carJson);

Car car = new Car();
while(!parser.isClosed()){
    JsonToken jsonToken = parser.nextToken();

    if(JsonToken.FIELD_NAME.equals(jsonToken)){
        String fieldName = parser.getCurrentName();
        System.out.println(fieldName);

        jsonToken = parser.nextToken();

        if("brand".equals(fieldName)){
            car.brand = parser.getValueAsString();
        } else if ("doors".equals(fieldName)){
            car.doors = parser.getValueAsInt();
        }
    }
}

System.out.println("car.brand = " + car.brand);
System.out.println("car.doors = " + car.doors);
```

如果指向的标记是字段名称，则 JsonParser 的 getCurrentName() 方法将返回当前字段名称。

如果指向的令牌是字符串字段值，则 getValueAsString() 返回当前令牌值作为字符串。 如果指向的令牌是整数字段值，则 getValueAsInt() 返回当前令牌值作为 int 值。 JsonParser 具有更多类似的方法来获取不同类型的 curren 令牌值（例如 boolean，short，long，float，double 等）。

六、JsonGenerator
===============

Jackson JsonGenerator 用于从 Java 对象（或代码从中生成 JSON 的任何数据结构）生成 JSON。

1、创建一个 JsonGenerator
--------------------

为了创建 Jackson JsonGenerator，必须首先创建 JsonFactory 实例。 这是创建 JsonFactory 的方法：

```java
JsonFactory factory = new JsonFactory();
```

一旦创建了 JsonFactory，就可以使用 JsonFactory 的 createGenerator() 方法创建 JsonGenerator。 这是创建 JsonGenerator 的示例：

```java
JsonFactory factory = new JsonFactory();

JsonGenerator generator = factory.createGenerator(
    new File("data/output.json"), JsonEncoding.UTF8);
```

createGenerator() 方法的第一个参数是生成的 JSON 的目标。 在上面的示例中，参数是 File 对象。 这意味着生成的 JSON 将被写入给定文件。 createGenerator() 方法已重载，因此还有其他版本的 createGenerator() 方法采用例如 OutputStream 等，提供了有关将生成的 JSON 写入何处的不同选项。

createGenerator() 方法的第二个参数是生成 JSON 时使用的字符编码。 上面的示例使用 UTF-8。

2、使用 JsonGenerator 生成 JSON
--------------------------

一旦创建了 JsonGenerator，就可以开始生成 JSON。 JsonGenerator 包含一组 write ...() 方法，可以使用这些方法来编写 JSON 对象的各个部分。 这是一个使用 Jackson JsonGenerator 生成 JSON 的简单示例：

```java
JsonFactory factory = new JsonFactory();

JsonGenerator generator = factory.createGenerator(
    new File("data/output.json"), JsonEncoding.UTF8);

generator.writeStartObject();
generator.writeStringField("brand", "Mercedes");
generator.writeNumberField("doors", 5);
generator.writeEndObject();

generator.close();
```

此示例首先调用 writeStartObject()，将 {写入输出。 然后，该示例调用 writeStringField()，将品牌字段名称 + 值写入输出。 之后，将调用 writeNumberField() 方法，此方法会将 Doors 字段名称 + 值写入输出。 最后，调用 writeEndObject()，将}写入输出。

JsonGenerator 还可以使用许多其他写入方法。 这个例子只显示了其中一些。

3、关闭 JsonGenerator
------------------

完成生成 JSON 后，应关闭 JsonGenerator。 您可以通过调用其 close() 方法来实现。 这是关闭 JsonGenerator 的样子：

```java
generator.close();
```

七、Jackson 注解
============

Jackson JSON 工具包包含一组 Java 注解，可以使用这些注解来设置将 JSON 读入对象的方式或从对象生成什么 JSON 的方式。 此 Jackson 注解教程介绍了如何使用 Jackson 的注解。

下面是一些常用的注解：

<table data-tool="mdnice编辑器" data-widget="datatable" cellspacing="0" cellpadding="0" border="0"><thead><tr><th colspan="1" rowspan="1">注解</th><th colspan="1" rowspan="1">用法</th></tr></thead><tbody><tr><td tabindex="0">@JsonProperty</td><td>用于属性，把属性的名称序列化时转换为另外一个名称。示例：<br>@JsonProperty("birth_ d ate")<br>private Date birthDate;</td></tr><tr><td tabindex="0">@JsonFormat</td><td>用于属性或者方法，把属性的格式序列化时转换成指定的格式。示例：<br>@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")<br>public Date getBirthDate()</td></tr><tr><td tabindex="0">@JsonPropertyOrder</td><td>用于类， 指定属性在序列化时 json 中的顺序 ， 示例：<br>@JsonPropertyOrder({"birth_Date", "name"})<br>public class Person</td></tr><tr><td tabindex="0">@JsonCreator</td><td>用于构造方法，和 @JsonProperty 配合使用，适用有参数的构造方法。 示例：<br>@JsonCreator<br>public Person(@JsonProperty("name")String name) {…}</td></tr><tr><td tabindex="0">@JsonAnySetter</td><td>用于属性或者方法，设置未反序列化的属性名和值作为键值存储到 map 中<br>@JsonAnySetter<br>public void set(String key, Object value) {<br>map.put(key, value);<br>}</td></tr><tr><td tabindex="0">@JsonAnyGetter</td><td>用于方法 ，获取所有未序列化的属性<br>public Map&lt;String, Object&gt; any() { return map; }</td></tr></tbody></table>

下面是一些注解的详细说明。

一）、Read + Write 注解
------------------

Jackson 包含一组注解，这些注解会影响从 JSON 读取 Java 对象以及将 Java 对象写入 JSON。 我将这些注解称为 “读 + 写注解”。 以下各节将更详细地介绍 Jackson 的读写注解。

### 1、@JsonIgnore

Jackson 注解 @JsonIgnore 用于告诉 Jackson 忽略 Java 对象的某个属性（字段）。 在将 JSON 读取到 Java 对象中以及将 Java 对象写入 JSON 时，都将忽略该属性。

这是使用 @JsonIgnore 注解的示例：

```java
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PersonIgnore {

    @JsonIgnore
    public long  personId = 0;

    public String name = null;
}
```

在上面的类中，不会从 JSON 读取或写入 JSON 属性 personId。

### 2、@JsonIgnoreProperties

@JsonIgnoreProperties Jackson 注解用于指定要忽略的类的属性列表。 @JsonIgnoreProperties 注解放置在类声明上方，而不是要忽略的各个属性（字段）上方。

这是如何使用 @JsonIgnoreProperties 注解的示例：

```java
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"firstName", "lastName"})
public class PersonIgnoreProperties {

    public long   personId = 0;

    public String  firstName = null;
    public String  lastName  = null;

}
```

在此示例中，属性 firstName 和 lastName 都将被忽略，因为它们的名称在类声明上方的 @JsonIgnoreProperties 注解声明内列出。

### 3、@JsonIgnoreType

@JsonIgnoreType Jackson 注解用于将整个类型（类）标记为在使用该类型的任何地方都将被忽略。

这是一个示例，展示如何使用 @JsonIgnoreType 注解：

```java
import com.fasterxml.jackson.annotation.JsonIgnoreType;

public class PersonIgnoreType {

    @JsonIgnoreType
    public static class Address {
        public String streetName  = null;
        public String houseNumber = null;
        public String zipCode     = null;
        public String city        = null;
        public String country     = null;
    }

    public long    personId = 0;

    public String  name = null;

    public Address address = null;
}
```

在上面的示例中，所有 Address 实例将被忽略。

### 4、@JsonAutoDetect

Jackson 注解 @JsonAutoDetect 用于告诉 Jackson 在读写对象时包括非 public 修饰的属性。

这是一个示例类，展示如何使用 @JsonAutoDetect 注解：

```java
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY )
public class PersonAutoDetect {

    private long  personId = 123;
    public String name     = null;

}
```

JsonAutoDetect.Visibility 类包含与 Java 中的可见性级别匹配的常量，表示 ANY，DEFAULT，NON_PRIVATE，NONE，PROTECTED_AND_PRIVATE 和 PUBLIC_ONLY。

二）、Read 注解
----------

Jackson 包含一组注解，这些注解仅影响 Jackson 将 JSON 解析为对象的方式 - 意味着它们影响 Jackson 对 JSON 的读取。 我称这些为 “读注解”。 以下各节介绍了 Jackson 的读注解。

### 1、@JsonSetter

Jackson 注解 @JsonSetter 用于告诉 Jackson，当将 JSON 读入对象时，应将此 setter 方法的名称与 JSON 数据中的属性名称匹配。 如果 Java 类内部使用的属性名称与 JSON 文件中使用的属性名称不同，这个注解就很有用了。

以下 Person 类用 personId 名称对应 JSON 中名为 id 的字段：

```java
public class Person {

    private long   personId = 0;
    private String name     = null;

    public long getPersonId() { return this.personId; }
    public void setPersonId(long personId) { this.personId = personId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

但是在此 JSON 对象中，使用名称 id 代替 personId：

```json
{
  "id"   : 1234,
  "name" : "John"
}
```

Jackson 无法将 id 属性从 JSON 对象映射到 Java 类的 personId 字段。

@JsonSetter 注解指示 Jackson 为给定的 JSON 字段使用 setter 方法。 在我们的示例中，我们在 setPersonId() 方法上方添加 @JsonSetter 注解。

这是添加 @JsonSetter 注解的实例：

```java
public class Person {

    private long   personId = 0;
    private String name     = null;

    public long getPersonId() { return this.personId; }
    @JsonSetter("id")
    public void setPersonId(long personId) { this.personId = personId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

@JsonSetter 注解中指定的值是要与此 setter 方法匹配的 JSON 字段的名称。 在这种情况下，名称为 id，因为这是我们要映射到 setPersonId()setter 方法的 JSON 对象中字段的名称。  

### 2、@JsonAnySetter

Jackson 注解 @JsonAnySetter 表示 Jackson 为 JSON 对象中所有无法识别的字段调用相同的 setter 方法。 “无法识别” 是指尚未映射到 Java 对象中的属性或设置方法的所有字段。

看一下这个 Bag 类：

```java
public class Bag {

    private Map<String, Object> properties = new HashMap<>();

    public void set(String fieldName, Object value){
        this.properties.put(fieldName, value);
    }

    public Object get(String fieldName){
        return this.properties.get(fieldName);
    }
}
```

然后查看此 JSON 对象：

```json
{
  "id"   : 1234,
  "name" : "John"
}
```

Jackson 无法直接将此 JSON 对象的 id 和 name 属性映射到 Bag 类，因为 Bag 类不包含任何公共字段或 setter 方法。

可以通过添加 @JsonAnySetter 注解来告诉 Jackson 为所有无法识别的字段调用 set() 方法，如下所示：

```java
public class Bag {

    private Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void set(String fieldName, Object value){
        this.properties.put(fieldName, value);
    }

    public Object get(String fieldName){
        return this.properties.get(fieldName);
    }
}
```

现在，Jackson 将使用 JSON 对象中所有无法识别的字段的名称和值调用 set() 方法。

请记住，这仅对无法识别的字段有效。 例如，如果您向 Bag Java 类添加了公共名称属性或 setName（String）方法，则 JSON 对象中的名称字段将改为映射到该属性 / 设置器。

### 3、@JsonCreator

Jackson 注解 @JsonCreator 用于告诉 Jackson 该 Java 对象具有一个构造函数（“创建者”），该构造函数可以将 JSON 对象的字段与 Java 对象的字段进行匹配。

@JsonCreator 注解在无法使用 @JsonSetter 注解的情况下很有用。 例如，不可变对象没有任何设置方法，因此它们需要将其初始值注入到构造函数中。

以这个 PersonImmutable 类为例：

```java
public class PersonImmutable {

    private long   id   = 0;
    private String name = null;

    public PersonImmutable(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
```

要告诉 Jackson 应该调用 PersonImmutable 的构造函数，我们必须在构造函数中添加 @JsonCreator 注解。 但是，仅凭这一点还不够。 我们还必须注解构造函数的参数，以告诉 Jackson 将 JSON 对象中的哪些字段传递给哪些构造函数参数。

添加了 @JsonCreator 和 @JsonProperty 注解的 PersonImmutable 类的示例如下：

```java
public class PersonImmutable {

    private long   id   = 0;
    private String name = null;

    @JsonCreator
    public PersonImmutable(
            @JsonProperty("id")  long id,
            @JsonProperty("name") String name  ) {

        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
```

请注意，构造函数上方的注解以及构造函数参数之前的注解。 现在，Jackson 能够从此 JSON 对象创建 PersonImmutable：

```java
{
  "id"   : 1234,
  "name" : "John"
}
```

### 4、@JacksonInject

Jackson 注解 @JacksonInject 用于将值注入到解析的对象中，而不是从 JSON 中读取这些值。 例如，假设正在从各种不同的源下载 Person JSON 对象，并且想知道给定 Person 对象来自哪个源。 源本身可能不包含该信息，但是可以让 Jackson 将其注入到根据 JSON 对象创建的 Java 对象中。

要将 Java 类中的字段标记为需要由 Jackson 注入其值的字段，请在该字段上方添加 @JacksonInject 注解。

这是一个示例 PersonInject 类，在属性上方添加了 @JacksonInject 注解：

```java
public class PersonInject {

    public long   id   = 0;
    public String name = null;

    @JacksonInject
    public String source = null;

}
```

为了让 Jackson 将值注入属性，需要在创建 Jackson ObjectMapper 时做一些额外的工作。

这是让 Jackson 将值注入 Java 对象的过程：

```java
InjectableValues inject = new InjectableValues.Std().addValue(String.class, "jenkov.com");
PersonInject personInject = new ObjectMapper().reader(inject)
                        .forType(PersonInject.class)
                        .readValue(new File("data/person.json"));
```

请注意，如何在 InjectableValues addValue() 方法中设置要注入到 source 属性中的值。 还要注意，该值仅绑定到字符串类型 - 而不绑定到任何特定的字段名称。 @JacksonInject 注解指定将值注入到哪个字段。

如果要从多个源下载人员 JSON 对象，并为每个源注入不同的源值，则必须为每个源重复以上代码。  

### 5、@JsonDeserialize

Jackson 注解 @JsonDeserialize 用于为 Java 对象中给定的属性指定自定义反序列化器类。

例如，假设想优化布尔值 false 和 true 的在线格式，使其分别为 0 和 1。

首先，需要将 @JsonDeserialize 注解添加到要为其使用自定义反序列化器的字段。 这是将 @JsonDeserialize 注解添加到字段的示例：

```java
public class PersonDeserialize {

    public long    id      = 0;
    public String  name    = null;

    @JsonDeserialize(using = OptimizedBooleanDeserializer.class)
    public boolean enabled = false;
}
```

其次，这是 @JsonDeserialize 注解中引用的 OptimizedBooleanDeserializer 类的实例：

```java
public class OptimizedBooleanDeserializer
    extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws
        IOException, JsonProcessingException {

        String text = jsonParser.getText();
        if("0".equals(text)) return false;
        return true;
    }
}
```

请注意，OptimizedBooleanDeserializer 类使用通用类型 Boolean 扩展了 JsonDeserializer。 这样做会使 deserialize() 方法返回一个布尔对象。 如果要反序列化其他类型（例如 java.util.Date），则必须在泛型括号内指定该类型。

可以通过调用 jsonParser 参数的 getText() 方法来获取要反序列化的字段的值。 然后，可以将该文本反序列化为任何值，然后输入反序列化程序所针对的类型（在此示例中为布尔值）。

最后，需要查看使用自定义反序列化器和 @JsonDeserializer 注解反序列化对象的格式：

```java
PersonDeserialize person = objectMapper
        .reader(PersonDeserialize.class)
        .readValue(new File("data/person-optimized-boolean.json"));
```

注意，我们首先需要如何使用 ObjectMapper 的 reader() 方法为 PersonDeserialize 类创建一个阅读器，然后在该方法返回的对象上调用 readValue()。

三）、Write 注解
-----------

Jackson 还包含一组注解，这些注解可以影响 Jackson 将 Java 对象序列化（写入）到 JSON 的方式。 以下各节将介绍这些写（序列化）注解中的每一个。

### 1、@JsonInclude

Jackson 注解 @JsonInclude 告诉 Jackson 仅在某些情况下包括属性。 例如，仅当属性为非 null，非空或具有非默认值时，才应包括该属性。 这是显示如何使用 @JsonInclude 注解的示例：

```java
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonInclude {

    public long  personId = 0;
    public String name     = null;

}
```

如果为该示例设置的值是非空的，则此示例将仅包括 name 属性，这意味着不为 null 且不是空字符串。

@JsonInclude 注解的一个更通俗的名称应该是 @JsonIncludeOnlyWhen，但是写起来会更长。

### 2、@JsonGetter

@JsonGetter Jackson 注解用于告诉 Jackson，应该通过调用 getter 方法而不是通过直接字段访问来获取某个字段值。 如果您的 Java 类使用 jQuery 样式的 getter 和 setter 名称，则 @JsonGetter 注解很有用。

例如，您可能拥有方法 personId() 和 personId（long id），而不是 getPersonId() 和 setPersonId()。

这是一个名为 PersonGetter 的示例类，它显示了 @JsonGetter 注解的用法：

```java
public class PersonGetter {

    private long  personId = 0;

    @JsonGetter("id")
    public long personId() { return this.personId; }

    @JsonSetter("id")
    public void personId(long personId) { this.personId = personId; }

}
```

如您所见，personId() 方法带有 @JsonGetter 注解。 @JsonGetter 注解上设置的值是 JSON 对象中应使用的名称。 因此，用于 JSON 对象中 personId 的名称是 id。 生成的 JSON 对象如下所示：

```json
{"id":0}
```

还要注意，personId（long personId）方法使用 @JsonSetter 注解进行注解，以使 Jackson 识别为与 JSON 对象中的 id 属性匹配的设置方法。 从 JSON 读取 Java 对象时使用 @JsonSetter 注解 - 将 Java 对象写入 JSON 时不使用。 为了完整起见，仅包含 @JsonSetter 注解。

### 3、@JsonAnyGetter

@JsonAnyGetter Jackson 注解使您可以将 Map 用作要序列化为 JSON 的属性的容器。 这是在 Java 类中使用 @JsonAnyGetter 注解的示例：

```java
public class PersonAnyGetter {

    private Map<String, Object> properties = new HashMap<>();

    @JsonAnyGetter
    public Map<String, Object> properties() {
        return properties;
    }
}
```

当看到 @JsonAnyGetter 注解时，Jackson 将从 @JsonAnyGetter 注解的方法中获取返回的 Map，并将该 Map 中的每个键值对都视为一个属性。 换句话说，Map 中的所有键值对都将作为 PersonAnyGetter 对象的一部分序列化为 JSON。

### 4、@JsonPropertyOrder

@JsonPropertyOrder Jackson 注解可用于指定将 Java 对象的字段序列化为 JSON 的顺序。 这是显示如何使用 @JsonPropertyOrder 注解的示例：

```java
@JsonPropertyOrder({"name", "personId"})
public class PersonPropertyOrder {

    public long  personId  = 0;
    public String name     = null;

}
```

通常，Jackson 会按照在类中找到的顺序序列化 PersonPropertyOrder 中的属性。 但是，@JsonPropertyOrder 注解指定了不同的顺序，在序列化的 JSON 输出中，name 属性将首先出现，personId 属性将随后出现。

### 5、@JsonRawValue

@JsonRawValue Jackson 注解告诉 Jackson 该属性值应直接写入 JSON 输出。 如果该属性是字符串，Jackson 通常会将值括在引号中，但是如果使用 @JsonRawValue 属性进行注解，Jackson 将不会这样做。

为了更清楚 @JsonRawValue 的作用，看看没有使用 @JsonRawValue 的此类：

```java
public class PersonRawValue {

    public long   personId = 0;

    public String address  = "$#";
}
```

Jackson 会将其序列化为以下 JSON 字符串：

```json
{"personId":0,"address":"$#"}
```

现在，我们将 @JsonRawValue 添加到 address 属性，如下所示：

```java
public class PersonRawValue {

    public long   personId = 0;

    @JsonRawValue
    public String address  = "$#";
}
```

现在，当对地址属性进行序列化时，杰克逊将省略引号。 因此，序列化的 JSON 如下所示：

```json
{"personId":0,"address":$#}
```

当然它是无效的 JSON，那么为什么要这么做呢？

如果 address 属性包含一个 JSON 字符串，那么该 JSON 字符串将被序列化为最终的 JSON 对象，作为 JSON 对象结构的一部分，而不仅是序列化为 JSON 对象的 address 字段中的字符串。

要查看其工作原理，让我们像下面这样更改 address 属性的值：

```java
public class PersonRawValue {

    public long   personId = 0;

    @JsonRawValue
    public String address  =
            "{ \"street\" : \"Wall Street\", \"no\":1}";

}
```

Jackson 会将其序列化为以下 JSON：

```
{"personId":0,"address":{ "street" : "Wall Street", "no":1}}
```

请注意，JSON 字符串现在如何成为序列化 JSON 结构的一部分。

没有 @JsonRawValue 注解，Jackson 会将对象序列化为以下 JSON：

```
{"personId":0,"address":"{ \"street\" : \"Wall Street\", \"no\":1}"}
```

请注意，address 属性的值现在如何用引号引起来，并且值内的所有引号均被转义。

### 6、@JsonValue

Jackson 注解 @JsonValue 告诉 Jackson，Jackson 不应该尝试序列化对象本身，而应在对象上调用将对象序列化为 JSON 字符串的方法。 请注意，Jackson 将在自定义序列化返回的 String 内转义任何引号，因此不能返回例如 完整的 JSON 对象。 为此，应该改用 @JsonRawValue（请参阅上一节）。

@JsonValue 注解已添加到 Jackson 调用的方法中，以将对象序列化为 JSON 字符串。 这是显示如何使用 @JsonValue 注解的示例：

```java
public class PersonValue {

    public long   personId = 0;
    public String name = null;

    @JsonValue
    public String toJson(){
        return this.personId + "," + this.name;
    }

}
```

要求 Jackson 序列化 PersonValue 对象所得到的输出是：

```
"0,null"
```

引号由 Jackson 添加。 请记住，对象返回的值字符串中的所有引号均会转义。

### 7、@JsonSerialize

@JsonSerialize Jackson 注解用于为 Java 对象中的字段指定自定义序列化程序。 这是一个使用 @JsonSerialize 注解的 Java 类示例：

```java
public class PersonSerializer {

    public long   personId = 0;
    public String name     = "John";

    @JsonSerialize(using = OptimizedBooleanSerializer.class)
    public boolean enabled = false;
}
```

注意启用字段上方的 @JsonSerialize 注解。

OptimizedBooleanSerializer 将序列的真值序列化为 1，将假值序列化为 0。这是代码：

```java
public class OptimizedBooleanSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean aBoolean, JsonGenerator jsonGenerator, 
        SerializerProvider serializerProvider) 
    throws IOException, JsonProcessingException {

        if(aBoolean){
            jsonGenerator.writeNumber(1);
        } else {
            jsonGenerator.writeNumber(0);
        }
    }
}
```

**参考：**

【1】：[Jackson Installation](https://link.juejin.cn?target=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-json%2Fjackson-objectmapper.html%23jackson-databind "http://tutorials.jenkov.com/java-json/jackson-objectmapper.html#jackson-databind") 【2】：[Jackson ObjectMapper](https://link.juejin.cn?target=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-json%2Fjackson-installation.html "http://tutorials.jenkov.com/java-json/jackson-installation.html") 【3】：[Jackson 框架的高阶应用](https://link.juejin.cn?target=https%3A%2F%2Fwww.ibm.com%2Fdeveloperworks%2Fcn%2Fjava%2Fjackson-advanced-application%2Findex.html "https://www.ibm.com/developerworks/cn/java/jackson-advanced-application/index.html") 【4】：[Jackson JsonNode](https://link.juejin.cn?target=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-json%2Fjackson-jsonnode.html "http://tutorials.jenkov.com/java-json/jackson-jsonnode.html") 【5】：[Jackson JsonParser](https://link.juejin.cn?target=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-json%2Fjackson-jsonparser.html "http://tutorials.jenkov.com/java-json/jackson-jsonparser.html") 【6】：[Jackson JsonGenerator](https://link.juejin.cn?target=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-json%2Fjackson-jsongenerator.html "http://tutorials.jenkov.com/java-json/jackson-jsongenerator.html") 【7】：[Jackson Annotations](https://link.juejin.cn?target=http%3A%2F%2Ftutorials.jenkov.com%2Fjava-json%2Fjackson-annotations.html "http://tutorials.jenkov.com/java-json/jackson-annotations.html")





# SpringBoot使用Jackson序列化Json和XML