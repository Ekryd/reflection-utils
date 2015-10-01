# Reflection-utils ![Icon](https://raw.githubusercontent.com/Ekryd/reflection-utils/master/misc/ReflectionUtils.png)

[![Build Status](https://travis-ci.org/Ekryd/reflection-utils.svg?branch=master)](https://travis-ci.org/Ekryd/reflection-utils)
[![Coverage Status](https://coveralls.io/repos/Ekryd/reflection-utils/badge.svg?branch=master)](https://coveralls.io/r/Ekryd/reflection-utils?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.reflection-utils/reflection-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ekryd.reflection-utils/reflection-utils)
[![Coverity](https://scan.coverity.com/projects/4608/badge.svg)](https://scan.coverity.com/projects/4608)

This library will make it easier to get and set internal fields in classes using java reflection. The library is optimized for convenience and not for speed, so it works best with unit tests.

Mocking is great! Sometimes the easiest way to insert mocks are by Java reflection. Especially if you cannot use constructor arguments or setter methods.

## Example of how to use the ReflectionHelper in a test ##
```
    @Test
    public void storeMethodShouldSaveEntity() throws Exception {
        // Setup entities
        EntityHandler handler = new EntityHandlerImpl();        
        StoreDao storeDao = mock(StoreDao.class);
        
        // *** This is the ReflectionHelper! You don't need to know the name of field nor the type, just set the instance ***
        new ReflectionHelper(handler).setField(storeDao);
        
        // Perform method
        handler.store();

        // Verify save method
        verify(storeDao).save();
    }
```

[See more](http://ekryd.github.io/reflection-utils/)
