
[[ -f ~/.Xresources ]] && xrdb -merge ~/.Xresources


exec xbindkeys -p & exec i3 #startdde
