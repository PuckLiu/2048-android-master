// Wait till the browser is ready to render the game (avoids glitches)
window.requestAnimationFrame(function () {
  size = document.getElementsByName("keywords")[0].content;
  console.log("size:"+size);
  var userAgentInfo = navigator.userAgent;
  var support = userAgentInfo.indexOf("android_2048") > 0
  if (support) {
    size = window.JavaScriptFunction.getLevel()
  }
  new GameManager(size, KeyboardInputManager, HTMLActuator, LocalStorageManager);
});
