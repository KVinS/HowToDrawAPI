/**
 * Created by Semyon on 23.03.2015.
 */


function SuperPaint() {

    var $container = null;
    var $layersSelect = null;
    var layers = [];

    var width = 0;
    var height = 0;

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
            alert( this.value ); // or $(this).val()
        });
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


        //drawing specific

        var clickX = [];
        var clickY = [];
        var clickDrag = [];
        var paint;

        this.mousedown = function(e) {
            var offset = $layer.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            paint = true;
            addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
            redraw();
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
            clickDrag.push(dragging);
        }

        function redraw(){
            context.clearRect(0, 0, context.canvas.width, context.canvas.height); // Clears the canvas

            context.strokeStyle = "#df4b26";
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
                context.stroke();
            }
        }

        return this;
    }

}