# Demo
![image](/screenshot/cusprogress.gif)
# usage
Add dependencies in build.gradle:
```
 dependencies {
       compile 'com.totcy.widget:customprogressview:1.0.0'
    }
```
Or Maven:
```
<dependency>
  <groupId>com.totcy.widget</groupId>
  <artifactId>customprogressview</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
java:
```
 final CustomProgressView progressView = (CustomProgressView) findViewById(R.id.cpv_demo);
        progressView.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressView.setCurProgressWithAnim(88);
            }
        }, 500);
```