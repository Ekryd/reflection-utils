# Reflection-utils ![Icon](https://raw.githubusercontent.com/Ekryd/reflection-utils/master/ReflectionUtils.png)

[![Build Status](https://travis-ci.org/Ekryd/reflection-utils.svg?branch=master)](https://travis-ci.org/Ekryd/reflection-utils)

This library will make it easier to get and set internal fields in classes using java reflection. The library is optimized for convenience and not for speed, so it works best with unit tests.

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

[See more](http://ekryd.github.io/reflection-utils/)
