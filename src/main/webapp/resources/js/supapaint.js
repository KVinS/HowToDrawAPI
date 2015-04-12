/**
 * Created by Semyon on 23.03.2015.
 */


function SuperPaint() {

    var $container = null;
    var $layersSelect = null;
    var layers = [];

    var width = 0;
    var height = 0;
    var mode = "normal";
    var color = "#000000";

    this.init = function(_container, _layersSelect) {
        $container = $(_container);
        $layersSelect = $(_layersSelect);
        width = $container.width();
        height = $container.height();
        this.addLayer("Главный слой");

        var __events = ['mousedown', 'mousemove', 'mouseup', 'mouseleave'];
        for (var i = 0; i < __events.length; i++) {
            var event = __events[i];
            $container.on(event, function(eventName) {
                return function(e) {
                    for (var j = 0; j < layers.length; j++) {
                        var layer = layers[j];
                        if (layer.isEnabled()) {
                            layer[eventName](e);
                        }
                    }
                };
            }(event));
        }
        $layersSelect.material_select();
        $layersSelect.on('change', function() {
        });
    };

    this.toggleFlood = function() {
        mode = (mode == "normal" ? "flood" : "normal");
    };

    this.setColor = function(hex) {
        color = "#" + hex;
    };

    this.addLayer = function(name) {
        var layer = createLayer(name).initLayer();

        var $option = $("<option value='" + name + "'>" + name + "</option>");

        $layersSelect.append($option);
        selectLayer(name);
        $layersSelect.material_select_update();
    };

    this.removeLayer = function(name) {
        for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            if (layer.name() == name) {
                layer.remove();
                var index = layers.indexOf(layer);
                if (index > -1) {
                    layers.splice(index, 1);
                }
                break;
            }
        }
        var o = $layersSelect.find("option[value='" + name + "']");
        o.remove();
        $layersSelect.material_select_update();
        selectLayerByNum(0);
    };

    function selectLayer(name) {
        for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            if (layer.name() == name) {
                layer.setEnabled(true);
            } else {
                layer.setEnabled(false);
            }
        }
        $layersSelect.val(name);
    }

    function selectLayerByNum(num) {
        for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            layer.setEnabled(false);
        }
        layers[num].setEnabled(true);
        $layersSelect.val(layers[num].name());
    }

    function createLayer(name) {
        var layerNum = layers.length;
        var layer = new Layer(layerNum, name, width, height);
        layers.push(layer);
        $container.append(layer.getCanvas());
        return layer;
    }

    function Layer(id, _name, _width, _height) {

        var _this = this;

        var name = _name;

        var width = _width;
        var height = _height;
        var enabled = false;

        var canvas = null;
        var context = null;

        this.offsetLeft = 0;
        this.offsetTop = 0;

        var $layer = $("<canvas id='layer_" + id + "' width='" + width + "' height='" + height + "'>");
        canvas = $layer[0];

        if(typeof G_vmlCanvasManager != 'undefined') {
            canvas = G_vmlCanvasManager.initElement(canvas);
        }
        context = canvas.getContext("2d");

        this.clear = function() {

        };

        this.name = function() {
            return name;
        };

        this.initLayer = function() {
            var offset = $layer.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            return _this;
        };

        this.isEnabled = function() {
            return enabled;
        };

        this.remove = function() {
            $layer.remove();
        };

        this.setEnabled = function(_enabled) {
            enabled = _enabled;
        };

        this.getCanvas = function() {
            return $layer;
        };

        this.getContext = function() {
            return context;
        };


        //drawing specific

        var clickX = [];
        var clickY = [];
        var clickColor = [];
        var clickDrag = [];
        var paint;

        this.mousedown = function(e) {
            if (mode == "normal") {
                var offset = $layer.offset();
                this.offsetLeft = offset.left;
                this.offsetTop = offset.top;
                paint = true;
                addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
                redraw();
            } else if (mode == "flood") {
                clickX = [];
                clickY = [];
                clickColor = [];
                clickDrag = [];
                fill(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, canvas, context);
            }
        };

        this.mousemove = function(e) {
            var offset = $layer.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            if(paint){
                addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
                redraw();
            }
        };

        this.mouseup = function(e){
            paint = false;
        };

        this.mouseleave = function(e){
            paint = false;
        };

        function addClick(x, y, dragging) {
            clickX.push(x);
            clickY.push(y);
            clickColor.push(color);
            clickDrag.push(dragging);
        }

        function redraw(){
//            context.clearRect(0, 0, context.canvas.width, context.canvas.height); // Clears the canvas

            context.lineJoin = "round";
            context.lineWidth = 25;

            for(var i=0; i < clickX.length; i++) {
                context.beginPath();
                if(clickDrag[i] && i){
                    context.moveTo(clickX[i-1], clickY[i-1]);
                }else{
                    context.moveTo(clickX[i]-1, clickY[i]);
                }
                context.lineTo(clickX[i], clickY[i]);
                context.closePath();
                context.strokeStyle = clickColor[i];
                context.stroke();
            }
            if (!clickDrag[clickX.length - 1]) {
                clickX = [];
                clickY = [];
                clickColor = [];
                clickDrag = [];
            } else {
                _splice(clickX);
                _splice(clickY);
                _splice(clickColor);
                _splice(clickDrag);
            }
        }

        return this;
    }

    function _splice(array) {
        array.splice(0, array.length - 2);
    }

    function fill(startX, startY, canvas, context) {

        var pixelStack = [[startX, startY]];
        var canvasHeight = canvas.height;
        var canvasWidth = canvas.width;
        var colorLayer = context.getImageData(0, 0, canvasWidth, canvasHeight);

        pixelPos = (startY * canvasWidth + startX) * 4;

        var startR = colorLayer.data[pixelPos];
        var startG = colorLayer.data[pixelPos + 1];
        var startB = colorLayer.data[pixelPos + 2];

        var c = hexToRgb(color);

        var fillColorR = c.r;
        var fillColorG = c.g;
        var fillColorB = c.b;

        if (fillColorR == startR && fillColorG == startG && fillColorB == startB) {
            return;
        }

        var drawingBoundTop = 0;


        while(pixelStack.length)
        {
            var newPos, x, y, pixelPos, reachLeft, reachRight;
            newPos = pixelStack.pop();
            x = newPos[0];
            y = newPos[1];

            pixelPos = (y*canvasWidth + x) * 4;
            while(y-- >= drawingBoundTop && matchStartColor(pixelPos))
            {
                pixelPos -= canvasWidth * 4;
            }
            pixelPos += canvasWidth * 4;
            ++y;
            reachLeft = false;
            reachRight = false;
            while(y++ < canvasHeight-1 && matchStartColor(pixelPos))
            {
                colorPixel(pixelPos);

                if(x > 0)
                {
                    if(matchStartColor(pixelPos - 4))
                    {
                        if(!reachLeft){
                            pixelStack.push([x - 1, y]);
                            reachLeft = true;
                        }
                    }
                    else if(reachLeft)
                    {
                        reachLeft = false;
                    }
                }

                if(x < canvasWidth-1)
                {
                    if(matchStartColor(pixelPos + 4))
                    {
                        if(!reachRight)
                        {
                            pixelStack.push([x + 1, y]);
                            reachRight = true;
                        }
                    }
                    else if(reachRight)
                    {
                        reachRight = false;
                    }
                }

                pixelPos += canvasWidth * 4;
            }
        }
        context.putImageData(colorLayer, 0, 0);

        function matchStartColor(pixelPos)
        {
            var r = colorLayer.data[pixelPos];
            var g = colorLayer.data[pixelPos+1];
            var b = colorLayer.data[pixelPos+2];

            return (r == startR && g == startG && b == startB);
        }

        function colorPixel(pixelPos)
        {
            colorLayer.data[pixelPos] = fillColorR;
            colorLayer.data[pixelPos+1] = fillColorG;
            colorLayer.data[pixelPos+2] = fillColorB;
            colorLayer.data[pixelPos+3] = 255;
        }
    }

}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}