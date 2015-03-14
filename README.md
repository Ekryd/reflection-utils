# reflection-utils
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

[See more](http://ekryd.github.io/reflection-utils/)
