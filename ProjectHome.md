This library will make it easier to get and set internal fields in classes using java reflection. The library is optimized for conveniance and not for speed, so it works best with unit tests.

Mocking is great! Sometimes the easiest way to insert mocks are by Java reflection. Especially if you cannot use constructor arguments or setter methods.

## Example ##
```
    @Test
    public void storeMethodShouldSaveEntity() throws Exception {
        // Setup entities
        EntityHandler handler = new EntityHandlerImpl();        
        StoreDao storeDao = mock(StoreDao.class);
        
        // Insert mock into handler
        new ReflectionHelper(handler).setField(storeDao);
        
        // Perform method
        handler.store();

        // Verify save method
        verify(storeDao).save();
    }
```

The ReflectionHelper will insert the mocked object into the handler
  * You don't need to use complicated reflection code.
  * You don't need to specify field name; if only one field fits then that field will be used.
  * If the field cannot be found, then user friendly exceptions are sent.

## Methods ##

The Reflection helper have 5 methods:

**ABC abc = ReflectionHelper.instantiatePrivateConstructor(ABC.class)** will create a new instance of ABC by invoking a (private) constructor with no arguments. This method can be used for getting hard-to-reach coverage for utility classes.

**new ReflectionHelper(abc).setField(value)** will insert the value into the instance abc. This method can be used to insert stubs or mocks into other classes. ReflectionHelper will determine which field that should be set by comparing class types or assignable types. Setting an interface type to a concrete class value is no problem.

**new ReflectionHelper(abc).setField("name", value)** will insert the value into the instance abc for a named field. This method can be used to insert setup parameters where there are multiple fields that have the same type.

**ValueClass value = new ReflectionHelper(abc).getField(ValueClass.class)** will return the current value for the field of a specified class.

**ValueClass value = (ValueClass) new ReflectionHelper(abc).getField("name")** will return the current value for a named field.

## Download ##

Maven users can add this library with the following addition to their pom.xml file.

```
  ...

  <dependencies>
    ...
    <dependency>
      <groupId>com.google.code.reflection-utils</groupId>
      <artifactId>reflection-utils</artifactId>
      <version>0.0.1</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  ...
```

## Scope ##

The visibility scope of the fields are like this:

  * Fields that are private, protected, package protected or public in the instance class can be set with the ReflectionHelper
  * Fields that are protected, package protected or public in the superclasses of the instance class can be set with the ReflectionHelper
  * Fields that are hidden (same name and type) by a subclass are not available to the ReflectionHelper
  * Internal fields in classes from Sun are not available
  * Final fields cannot be changed
  * If two or more fields can be set by the same value, then the name of the field must be specified

## Why ##

This framework was created for the following reasons:
  * I don't like setting fields to package protected just because some unit tests have to change values
  * I don't like extra setter-methods, that are only placed in the class just because some unit tests have to change values
  * Spring is very nice, but maintaining large xml configurations for tests can be difficult
  * I do like test coverage even if I am working with legacy code which cannot or must not be changed
  * Setting and getting values by class type instead of name helps a lot when refactoring code
  * This reflection code has been used by me since 2008 and it makes sense to gather it into a framework